package cn.sheeva.search.query.booleanquery.ast;

import java.util.Set;
import java.util.TreeSet;

import cn.sheeva.index.Index;
import cn.sheeva.search.query.booleanquery.BooleanQuerySign;

public class OrAST extends ABooleanQueryAST {
    ABooleanQueryAST l;
    ABooleanQueryAST r;
    
    public OrAST(ABooleanQueryAST l,ABooleanQueryAST r) {
        this.l=l;
        this.r=r;
    }

    @Override
    public String toString() {
        return l.toString()+" "+BooleanQuerySign.OR.toString()+" "+r.toString();
    }

    @Override
    public Set<Long> getMatchDocIds(Index index) {
        Set<Long> lIds=l.getMatchDocIds(index);
        Set<Long> rIds=r.getMatchDocIds(index);
        
        Set<Long> union=new TreeSet<>(lIds);
        union.addAll(rIds);
        return union;
    }
}
