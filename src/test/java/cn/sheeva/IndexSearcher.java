package cn.sheeva;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import cn.sheeva.doc.Doc;
import cn.sheeva.index.Indexer;
import cn.sheeva.search.Searcher;
import cn.sheeva.search.SearcherPool;
import cn.sheeva.token.ComplexTokenizer;
import cn.sheeva.token.SimpleTokenizer;
import cn.sheeva.util.LogUtil;
import cn.sheeva.util.TimeProfiler;
import cn.sheeva.index.DocMap;
import cn.sheeva.index.Index;

public class IndexSearcher extends ASearcher {
    private Indexer indexer;
    private Searcher searcher;
    
    public IndexSearcher(String indexdir,String indexname) {
        indexer=new Indexer(indexdir,indexname,new SimpleTokenizer());
        deleteIndex();
        index();
        searcher=SearcherPool.getSearcher(indexer.index);
    }

    @Override
    public void search(String word){
        List<Doc> foundDocs=searcher.search(word);
        
        List<String> foundFiles=new LinkedList<>();
        for (Doc doc : foundDocs) {
            foundFiles.add(new File(doc.filePath).getName());
        }
        
        showResult(word, foundFiles);
    }
    
    public void index(){
        TimeProfiler.begin();
        int bulkThreshold=500;
        int indexed=0;
        List<Doc> docs=new LinkedList<>();
        for (File file : dir.listFiles()) {
            Doc doc=new Doc(file.getPath());
            docs.add(doc);
            if (docs.size()==bulkThreshold) {
                try {
                    indexer.add(docs);
                } catch (IOException e) {
                    LogUtil.err("add doc fail,docs: "+docs,e);
                }
                docs=new LinkedList<>();
                indexed+=bulkThreshold;
                System.err.println("===================已索引文档"+indexed+"篇");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {Thread.currentThread().interrupt();}
            }
        }
        
        long time=TimeProfiler.end();
        indexer.hardCommit();
        System.out.println("索引用时："+time+"ms, ("+time/1000+"s)"+"\n\n");
    }
    
    public void deleteIndex(){
        indexer.deleteIndex();
    }
    

}
