package cn.sheeva.search;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import cn.sheeva.doc.Doc;
import cn.sheeva.index.Index;
import cn.sheeva.util.LogUtil;

public class Searcher {
    private Index index;
    public Searcher(Index index) {
        this.index=index.copy();
    }
    
    public List<Doc> search(String word){
        List<Doc> foundDocs=new LinkedList<>();

        TreeSet<Long> docIds=index.invertIndex.get(word);
        
        if (docIds!=null) {
            for (Long docId : docIds) {
                Doc doc=index.docMap.get(docId);
                if (doc!=null) {
                    foundDocs.add(doc);
                }else {
                    LogUtil.err("can't get doc from docMap,id: "+docId);
                }
            }
        }
        
        return foundDocs;
    }
}
