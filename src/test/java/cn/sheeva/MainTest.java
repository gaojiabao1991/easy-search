package cn.sheeva;

import cn.sheeva.config.Config;

public class MainTest {
    private static ScanSearcher scanSearcher=new ScanSearcher();
    private static IndexSearcher indexSearcher=new IndexSearcher(Config.curConfigSet.getIndexname());
    
    public static void main(String[] args) {
        String[] searchWords=new String[]{"搜索"};
        
//        scanSearcher.showTimeProfiler(searchWords);
        indexSearcher.deleteIndex();
        indexSearcher.index();
        indexSearcher.showTimeProfiler(searchWords);
    }
}
