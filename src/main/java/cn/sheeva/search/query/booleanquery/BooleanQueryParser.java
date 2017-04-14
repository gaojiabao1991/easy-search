package cn.sheeva.search.query.booleanquery;


import java.util.Arrays;

import cn.sheeva.search.query.booleanquery.ast.ABooleanQueryAST;
import cn.sheeva.search.query.booleanquery.ast.AndAST;
import cn.sheeva.search.query.booleanquery.ast.BracketsAST;
import cn.sheeva.search.query.booleanquery.ast.NotAST;
import cn.sheeva.search.query.booleanquery.ast.OrAST;
import cn.sheeva.search.query.booleanquery.ast.WordAST;

/**
 * 表达式文法：
 *  Exp:BracketsExp|AndExp|OrExp|NotExp|WordExp
    BracketsExp: '(' Exp ')'
    AndExp: Exp And Exp
    OrExp: Exp Or Exp
    NotExp: Not Exp
    WordExp: string
 * @author sheeva
 *
 */

public class BooleanQueryParser {
    
    /**
     * 需要判断5种情况分别对应5种子表达式
     * 递归调用
     * @createTime：2017年4月13日 
     * @author: gaojiabao
     */
    public static ABooleanQueryAST getAST(String[] arr){
        if (arr.length==0) {
            throw new RuntimeException("illegal exp: len==0");
        }
        
        /**
         * case 2: WordExp
         */
        if (arr.length==1) {
            if (BooleanQuerySign.isSign(arr[0])) {
                throw new RuntimeException("illegal exp: len==1 but not WordExp: "+arr[0]);
            }
            return new WordAST(arr[0]);
        }
        
        
        /**
         * case 1: BracketsExp
         */
        if (arr[0].equals(BooleanQuerySign.LB.toString())&&arr[arr.length-1].equals(BooleanQuerySign.RB.toString())) {
            boolean ifBracketExp;
            int rBracket=0;
            //iterator subArr[1,arr.leng-1]
            for (int i = arr.length-2; i >=1; i--) {
                String e=arr[i];
                if (e.equals(BooleanQuerySign.RB.toString())) {
                    rBracket++;
                }else if (e.equals(BooleanQuerySign.LB.toString())) {
                    rBracket--;
                }
                if (rBracket<0) {
                    //case: ( 中国 AND 人民 ) OR ( 人民 AND 国旗 )
                    ifBracketExp=false;
                    break;
                }
            }
            ifBracketExp=rBracket==0;
            
            if (ifBracketExp) {
                return new BracketsAST(getAST(Arrays.copyOfRange(arr, 1, arr.length-1)));
            }
        }

        
        /**
         * case 3: NotExp
         */
        if (arr.length==2) {
            if (!arr[0].equals(BooleanQuerySign.NOT.toString())) {
                throw new RuntimeException("illegal exp: len==2 but not NotExp: "+Arrays.asList(arr));
            }
            return new NotAST(new WordAST(arr[1]));
        }
        
        /**
         * case 4 and case 5: AndExp/OrExp
         */
        int rBracket=0;
        for (int i = arr.length-1; i >=0; i--) {
            String expElementStr=arr[i];
            if (expElementStr.equals(BooleanQuerySign.RB.toString())) {
                rBracket++;
            }else if (expElementStr.equals(BooleanQuerySign.LB.toString())) {
                rBracket--;
            }else if (rBracket==0) {
                if (expElementStr.equals(BooleanQuerySign.AND.toString())) {
                    return new AndAST(getAST(Arrays.copyOfRange(arr, 0, i)), getAST(Arrays.copyOfRange(arr, i+1, arr.length)));
                }else if (expElementStr.equals(BooleanQuerySign.OR.toString())) {
                    return new OrAST(getAST(Arrays.copyOfRange(arr, 0, i)), getAST(Arrays.copyOfRange(arr, i+1, arr.length)));
                }
            }
            
                    
        }
        
        throw new RuntimeException("unknown type exp: "+Arrays.asList(arr));
    }
}
