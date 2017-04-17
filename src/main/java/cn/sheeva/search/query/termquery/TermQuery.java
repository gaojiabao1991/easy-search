package cn.sheeva.search.query.termquery;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import cn.sheeva.doc.Doc;
import cn.sheeva.index.Index;
import cn.sheeva.search.query.IQuery;
import cn.sheeva.util.LogUtil;

public class TermQuery implements IQuery {
    private String field;
    private String term;
    
    public TermQuery(String field,String term) {
        this.field=field;
        this.term=term;
    }

    @Override
    public List<Doc> query(Index index) {
        List<Doc> foundDocs=new LinkedList<>();

        TreeSet<Long> docIds=index.invertIndexCollection.get(field).get(term);
        
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
