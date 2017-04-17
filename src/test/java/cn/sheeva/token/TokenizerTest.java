package cn.sheeva.token;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cn.sheeva.search.token.ComplexTokenizer;

public class TokenizerTest {
    /**
     * 多线程分词安全测试
     * @createTime：2017年3月27日 
     * @author: gaojiabao
     */
    @Test
    public void tokenTest(){
        ComplexTokenizer tokenizer=new ComplexTokenizer();
        List<String> tokens=tokenizer.getTokens("搜索互联网search\n我爱中国");
        Assert.assertEquals("[搜索, 互联网, search, 我, 爱, 中国]", tokens.toString());
        
    }
}
