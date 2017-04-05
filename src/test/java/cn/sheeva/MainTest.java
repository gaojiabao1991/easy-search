package cn.sheeva;

import org.junit.Test;

import cn.sheeva.config.Config;

public class MainTest {
//    private String[] searchWords=new String[]{"搜索","地位"};
    
//    @Test
//    public void scanSearcherTest(){
//        ScanSearcher scanSearcher=new ScanSearcher();
//        scanSearcher.showTimeProfiler(searchWords);
//    }
//    
//    @Test
//    public void indexSearcherTest(){
//        IndexSearcher indexSearcher=new IndexSearcher(Config.indexdir,Config.indexname);
//        indexSearcher.showTimeProfiler(searchWords);
//    }
    
    public static void main(String[] args) {
        String[] searchWords=new String[]{"搜索","地位"};
        IndexSearcher indexSearcher=new IndexSearcher(Config.indexdir,Config.indexname);
        indexSearcher.showTimeProfiler(searchWords);
    }
    
    
}
