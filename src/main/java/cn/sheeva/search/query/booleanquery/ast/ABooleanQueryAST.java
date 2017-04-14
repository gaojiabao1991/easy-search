package cn.sheeva.search.query.booleanquery.ast;

import java.util.Set;

import cn.sheeva.index.Index;

/**
 * 表达式文法：<BR/>
 * 
 * Exp:BracketsExp|AndExp|OrExp|NotExp|WordExp  <BR/>
 * BracketsExp: '(' Exp ')' <BR/>
 * AndExp: Exp And Exp  <BR/>
 * OrExp: Exp Or Exp    <BR/>
 * NotExp: Not Exp  <BR/>
 * WordExp: string  <BR/>
 * 
 * @author sheeva
 *
 */
public abstract class ABooleanQueryAST {
    public abstract Set<Long> getMatchDocIds(Index index);
    
    public abstract String toString();
}
