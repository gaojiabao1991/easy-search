package cn.sheeva.search;

import java.util.List;

import cn.sheeva.doc.Doc;
import cn.sheeva.index.Index;
import cn.sheeva.search.query.IQuery;

public class Searcher {
    private Index index;
    public Searcher(Index index) {
        this.index=index.copy();
    }
    
    public List<Doc> search(IQuery query){
        return query.query(index);
    }
}
