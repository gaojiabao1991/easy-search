package cn.sheeva.index;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import cn.sheeva.config.Config;
import cn.sheeva.doc.Doc;
import cn.sheeva.token.ITokenizer;
import cn.sheeva.util.SerializeUtil;

public class Indexer implements IIndexer,Serializable{
    private static final long serialVersionUID = 1L;

    private ITokenizer tokenizer;
    public Index index;
    
    /**
     * 同一个indexName同一时间只能只能打开一个Indexer对象，否则可能会造成索引损坏
     * TODO 添加一个文件锁强制拒绝多个Indexer打开同一个索引
     */
    public Indexer(String indexdir,String indexname,ITokenizer tokenizer) {
        this.tokenizer=tokenizer;
        this.index=new Index(indexdir, indexname);
        
    }
    
    /**
     * 添加索引串行执行
     * @throws IOException 
     * @createTime：2017年3月27日 
     * @author: gaojiabao
     */
    public synchronized void add(List<Doc> docs){
        for (Doc doc : docs) {
            add2RAM(doc);
        }
    }
    
    public synchronized void add(Doc doc){
        add2RAM(doc);
    }
    
    private synchronized void add2RAM(Doc doc) {
        long freeRam=Runtime.getRuntime().freeMemory()/1000/1000;
        if (freeRam<Config.autoCommitRamThreshold) {
            System.out.println("freeRam: "+freeRam);
            this.hardCommit();
            System.gc();
        }
        
        long id=index.docMap.add(doc);
        List<String> lines;
        try {
            lines = FileUtils.readLines(new File(doc.filePath));
        } catch (IOException e) {
            throw new RuntimeException("open doc file fail: "+doc.filePath);
        }
        for (String line : lines) {
            if (StringUtils.isNotEmpty(line)) {
                List<String> tokens=tokenizer.getTokens(line);
                for (String token : tokens) {
                    if (!index.invertIndex.containsFromRam(token)) {
                        TreeSet<Long> docSet=new TreeSet<>();
                        docSet.add(id);
                        index.invertIndex.put(token, docSet);
                    }else {
                        index.invertIndex.getFromRam(token).add(id);
                    }
                }
            }
        }
        System.out.println("已索引文档： "+doc.filePath);
    }
    
    public void hardCommit(){
        index.persist();
    }
    
    
//    public void delete(List<Doc> docs){
//        
//    }
    
    public synchronized void delete(Doc doc){
        
    }
    
//    public void update(List<Doc> docs){
//        
//    }
    
    public synchronized void update(Doc doc){
        
    }
    
    /**
     * 删除索引文件
     * @createTime：2017年3月29日 
     * @author: gaojiabao
     */
    public synchronized void deleteIndex(){
        index.clear();
    }
    
}
