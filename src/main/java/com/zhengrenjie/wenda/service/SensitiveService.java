package com.zhengrenjie.wenda.service;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class SensitiveService implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    /**
     * 默认敏感词替换符
     */
    private static final String DEFAULT_REPLACEMENT = "**";

    private class TrieNode{
        private boolean end = false;

        private Map<Character,TrieNode> subNodes = new HashMap<>();

        void addNode(Character key,TrieNode trieNode){
            subNodes.put(key,trieNode);
        }

        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        boolean isKeywordEnd() {
            return end;
        }

        void setKeywordEnd(boolean end) {
            this.end = end;
        }

        public int getSubNodeCount() {
            return subNodes.size();
        }

    }

    /**
     * 根节点
     */
    private TrieNode rootNode = new TrieNode();

    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    /**
     * 过滤敏感词
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        StringBuffer result = new StringBuffer();

        TrieNode tempNode = rootNode;
        int begin = 0; // 回滚数
        int position = 0; // 当前比较的位置

        while (position < text.length()){
            Character c = text.charAt(position);

            if(isSymbol(c)){
                if(tempNode == rootNode){
                    result.append(c);
                    ++begin;
                }
                position++;
                continue;
            }

            TrieNode trieNode = tempNode.getSubNode(c);

            if(trieNode == null){
                result.append(text.charAt(begin));
                begin ++;
                position = begin;
                tempNode = rootNode;
            } else if (trieNode.isKeywordEnd()) {
                //发现敏感词
                begin = position + 1;
                position = begin;
                tempNode = rootNode;
                result.append(DEFAULT_REPLACEMENT);
            } else {
                position++;
                tempNode = trieNode;
            }
        }

        result.append(text.substring(begin));
        return result.toString();
    }

    private void addWord(String lineTxt) {
        TrieNode tempNode = rootNode;
        for(int i = 0;i<lineTxt.length();i++){
            Character c = lineTxt.charAt(i);
            // 过滤空格
            if (isSymbol(c)) {
                continue;
            }

            TrieNode trieNode = tempNode.getSubNode(c);

            if(trieNode == null){
                trieNode = new TrieNode();
                tempNode.addNode(c,trieNode);
            }

            tempNode = trieNode;

            if(i == lineTxt.length() - 1){
                tempNode.setKeywordEnd(true);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        rootNode = new TrieNode();

        try {
            InputStream is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                lineTxt = lineTxt.trim();
                addWord(lineTxt);
            }
            read.close();
        } catch (Exception e) {
            logger.error("读取敏感词文件失败" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SensitiveService sensitiveService = new SensitiveService();
        sensitiveService.addWord("色情");
        sensitiveService.addWord("赌博");
        System.out.println(sensitiveService.filter("  你  好  赌 博"));
    }
}
