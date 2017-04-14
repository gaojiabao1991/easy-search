package cn.sheeva.search.query.booleanquery.ast;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import cn.sheeva.doc.Doc;
import cn.sheeva.index.Index;
import cn.sheeva.search.query.booleanquery.BooleanQuerySign;

public class NotAST extends ABooleanQueryAST {
    ABooleanQueryAST v;
    
    public NotAST(ABooleanQueryAST v) {
        this.v=v;
    }

    @Override
    public String toString() {
        return BooleanQuerySign.NOT.toString()+" "+v.toString();
    }

    @Override
    public Set<Long> getMatchDocIds(Index index) {
        Set<Long> allDocIds=index.docMap.getAllIds();
        Set<Long> exclude=v.getMatchDocIds(index);
        
        Set<Long> left=new TreeSet<>(allDocIds);
        left.removeAll(exclude);
        return left;
    }

}
