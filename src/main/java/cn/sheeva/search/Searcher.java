package cn.sheeva.search;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import cn.sheeva.doc.Doc;
import cn.sheeva.index.Index;

public class Searcher {
    private Index index;
    public Searcher(Index index) {
        this.index=index.regenerate();
    }
    
    public List<Doc> search(String word){
        List<Doc> foundDocs=new LinkedList<>();

        TreeSet<Long> docIds=index.invertIndex.get(word);
        
        if (docIds!=null) {
            for (Long docId : docIds) {
                foundDocs.add(index.docMap.get(docId));
            }
        }
        
        return foundDocs;
    }
}
