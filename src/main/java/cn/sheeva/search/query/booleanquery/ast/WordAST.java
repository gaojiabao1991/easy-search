package cn.sheeva.search.query.booleanquery.ast;

import java.util.Set;

import cn.sheeva.index.Index;

public class WordAST extends ABooleanQueryAST {
    String word;
    
    public WordAST(String word) {
        this.word=word;
    }

    @Override
    public String toString() {
        return word;
    }

    @Override
    public Set<Long> getMatchDocIds(Index index) {
        String[] arr=word.split(":");
        String field=arr[0];
        String value=arr[1];
        
        return index.invertIndexCollection.get(field).get(value);
    }
    
    
}
