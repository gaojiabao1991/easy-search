package cn.sheeva;

public class TestMain {
    private static String[] searchWords=new String[]{"搜索","search"};
    private static ScanSearcher scanSearcher=new ScanSearcher(searchWords);
    private static IndexSearcher indexSearcher=new IndexSearcher(searchWords);
    
    public static void main(String[] args) {
        scanSearcher.showTimeProfiler();
        indexSearcher.showTimeProfiler();
    }
}
