package cn.sheeva.index.invertindex.impl;

import cn.sheeva.index.invertindex.InvertIndex;

public class StringInvertIndex extends InvertIndex<String> {

    public StringInvertIndex(String path) {
        super(path);
    }

    @Override
    protected String parseT(String word) {
        return word;
    }

    @Override
    protected String word2String(String word) {
        return word;
    }

    @Override
    protected int compareTo(String word1, String word2) {
        return word1.compareTo(word2);
    }

}
