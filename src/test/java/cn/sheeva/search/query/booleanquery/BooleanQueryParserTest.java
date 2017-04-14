package cn.sheeva.search.query.booleanquery;


import org.junit.Assert;
import org.junit.Test;


public class BooleanQueryParserTest {
    @Test
    public void test(){
        String[] testCases=new String[]{
                "中国 AND 人民",
                "中国 OR 人民",
                "NOT 人民",
                "中国 AND NOT 人民",
                "中国 OR 人民 AND 国旗",
                "中国 AND ( 人民 OR 国旗 )",
                "( 中国 AND ( 人民 OR 国旗 ) )",
                "( 中国 AND 人民 ) OR ( 人民 AND 国旗 )",
                "A OR (B AND (C AND D))",
                "A OR NOT (B AND (C AND D))",
        };
        
        for (String exp : testCases) {
            String[] arr=exp.split(" ");
            Assert.assertEquals(exp, BooleanQueryParser.getAST(arr).toString());
            System.out.println("Pass: "+exp);
        }
    }
}
