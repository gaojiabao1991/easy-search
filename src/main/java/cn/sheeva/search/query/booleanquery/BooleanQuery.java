package cn.sheeva.search.query.booleanquery;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cn.sheeva.doc.Doc;
import cn.sheeva.index.Index;
import cn.sheeva.search.query.IQuery;
import cn.sheeva.search.query.booleanquery.ast.ABooleanQueryAST;

public class BooleanQuery implements IQuery {
    private ABooleanQueryAST ast;
    
    public BooleanQuery(String query) {
        String[] arr=query.split(" ");
        this.ast=BooleanQueryParser.getAST(arr);
    }
    
    public List<Doc> query(Index index){
        Set<Long> docIds=ast.getMatchDocIds(index);
        List<Doc> docs=new LinkedList<>();
        for (Long docId : docIds) {
            docs.add(index.docMap.get(docId));
        }
        return docs;
    } 
    
    @Override
    public String toString() {
        return ast.toString();
    }
}
