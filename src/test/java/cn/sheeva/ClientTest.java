package cn.sheeva;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import cn.sheeva.doc.Doc;
import cn.sheeva.index.Index;
import cn.sheeva.index.Indexer;
import cn.sheeva.search.Searcher;
import cn.sheeva.search.SearcherPool;
import cn.sheeva.token.SimpleTokenizer;
import cn.sheeva.util.LogUtil;
import cn.sheeva.util.TimeProfiler;

public class ClientTest {
    
    private static List<Doc> search(String word,Index index){
        Searcher searcher=SearcherPool.getSearcher(index);
        List<Doc> foundDocs=searcher.search(word);
        return foundDocs;
        
    }
    
    public static void index(String dataPath,Indexer indexer){
        TimeProfiler.begin();
        
        File dir=new File(dataPath);
        int bulkThreshold=500;
        int indexed=0;
        List<Doc> docs=new LinkedList<>();
        for (File file : dir.listFiles()) {
            Doc doc=new Doc(file.getPath());
            docs.add(doc);
            if (docs.size()==bulkThreshold) {
                indexer.add(docs);
                docs=new LinkedList<>();
                indexed+=bulkThreshold;
                System.err.println("===================已索引文档"+indexed+"篇");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {Thread.currentThread().interrupt();}
            }
        }
        if (!docs.isEmpty()) {
            indexer.add(docs);
            indexed+=docs.size();
            System.err.println("===================已索引文档"+indexed+"篇");
        }
        
        long time=TimeProfiler.end();
        indexer.hardCommit();
        System.out.println("索引用时："+time+"ms, ("+time/1000+"s)"+"\n\n");
    }
    
    public static void main(String[] args) {
        String word="搜索";
        Indexer indexer=new Indexer(TestConfig.indexdir, TestConfig.indexname, new SimpleTokenizer());
        
        indexer.deleteIndex();
        index(TestConfig.dataPath, indexer);
        
        TimeProfiler.begin();
        List<Doc> foundDocs=search(word, indexer.index);
        long time=TimeProfiler.end();
        
        System.out.println("====================搜索结果列表：===========================");
        for (Doc doc : foundDocs) {
            System.out.println(new File(doc.filePath).getName());
        }
        System.out.println("============搜索词：‘"+word+"’, 搜索用时："+time+"ms("+time/1000+"s)"+",找到文档"+foundDocs.size()+" 篇===========");
    }
}
