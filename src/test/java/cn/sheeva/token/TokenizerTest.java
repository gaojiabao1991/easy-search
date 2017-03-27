package cn.sheeva.token;

import org.junit.Test;

public class TokenizerTest {
    /**
     * 多线程分词安全测试
     * @createTime：2017年3月27日 
     * @author: gaojiabao
     */
    @Test
    public void tokenTest(){
        for (int i = 0; i < 100; i++) {
            Thread t=new Thread(new Runnable() {
                @Override
                public void run() {
                    Tokenizer tokenizer=new Tokenizer();
                    for (int j = 0; j < 1000; j++) {
                        System.out.println(tokenizer.getTokens("搜索互联网search"));
                    }
                }
            });
            t.start();
        }
        while (Thread.currentThread().getThreadGroup().activeCount()>2) {
            Thread.currentThread().yield();
        }
    }
}
