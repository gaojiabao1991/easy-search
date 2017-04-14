package cn.sheeva.search.query.booleanquery.ast;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import cn.sheeva.doc.Doc;
import cn.sheeva.index.Index;
import cn.sheeva.search.query.booleanquery.BooleanQuerySign;

public class AndAST extends ABooleanQueryAST {
    ABooleanQueryAST l;
    ABooleanQueryAST r;
    
    public AndAST(ABooleanQueryAST l,ABooleanQueryAST r) {
        this.l=l;
        this.r=r;
    }

    @Override
    public String toString() {
        return l.toString()+" "+BooleanQuerySign.AND.toString()+" "+r.toString();
    }

    @Override
    public Set<Long> getMatchDocIds(Index index) {
        Set<Long> lIds=l.getMatchDocIds(index);
        Set<Long> rIds=r.getMatchDocIds(index);
        
        Set<Long> cross=new TreeSet<>(lIds);
        cross.retainAll(rIds);
        
        return cross;
    }

}
