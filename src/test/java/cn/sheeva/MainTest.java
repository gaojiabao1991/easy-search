package cn.sheeva;

public class MainTest {
    private static ScanSearcher scanSearcher=new ScanSearcher();
    private static IndexSearcher indexSearcher=new IndexSearcher("book_index");
    
    public static void main(String[] args) {
        String[] searchWords=new String[]{"搜索","search"};
        
        scanSearcher.showTimeProfiler(searchWords);
//        indexSearcher.index();
        indexSearcher.showTimeProfiler(searchWords);
    }
}
