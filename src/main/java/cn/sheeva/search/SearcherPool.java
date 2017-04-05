package cn.sheeva.search;

import java.util.HashMap;

import cn.sheeva.index.Index;

public class SearcherPool {
    private static HashMap<Index, Searcher> searchers=new HashMap<>();
    
    public static synchronized Searcher getSearcher(Index index){
        if (!searchers.containsKey(index)) {
            searchers.put(index, new Searcher(index));
        }
        return searchers.get(index);
    }
}
