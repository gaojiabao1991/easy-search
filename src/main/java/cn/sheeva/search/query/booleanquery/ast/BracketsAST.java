package cn.sheeva.search.query.booleanquery.ast;

import java.util.List;
import java.util.Set;

import cn.sheeva.doc.Doc;
import cn.sheeva.index.Index;
import cn.sheeva.search.query.booleanquery.BooleanQuerySign;

public class BracketsAST extends ABooleanQueryAST{
    ABooleanQueryAST v;
    
    public BracketsAST(ABooleanQueryAST v) {
        this.v=v;
    }

    @Override
    public String toString() {
        return BooleanQuerySign.LB.toString()+" "+v.toString()+" "+BooleanQuerySign.RB.toString();
    }

    @Override
    public Set<Long> getMatchDocIds(Index index) {
        return v.getMatchDocIds(index);
    }

    
    
}
