package cn.sheeva.index.invertindex.impl;

import cn.sheeva.index.invertindex.InvertIndex;

public class IntInvertIndex extends InvertIndex<Integer> {

    public IntInvertIndex(String path) {
        super(path);
    }

    @Override
    protected Integer parseT(String word) {
        return Integer.parseInt(word);
    }

    @Override
    protected String word2String(Integer word) {
        return word.toString();
    }

    @Override
    protected int compareTo(Integer word1, Integer word2) {
        return word1-word2;
    }

}
