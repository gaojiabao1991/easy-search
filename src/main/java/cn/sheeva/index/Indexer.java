package cn.sheeva.index;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;

import cn.sheeva.doc.Doc;
import cn.sheeva.doc.DocIdMap;
import cn.sheeva.token.ITokenizer;
import cn.sheeva.token.ComplexTokenizer;
import cn.sheeva.util.ResourceUtil;
import cn.sheeva.util.SerializeUtil;

public class Indexer implements IIndexer,Serializable{
    private static final long serialVersionUID = 1L;

    private String indexPath;
    private SerializeUtil<Index> serializeUtil=new SerializeUtil<>();

    private ITokenizer tokenizer;
    public Index index;
    
    /**
     * 同一个indexName同一时间只能只能打开一个Indexer对象，否则可能会造成索引损坏
     * TODO 添加一个文件锁强制拒绝多个Indexer打开同一个索引
     */
    public Indexer(String indexPath,ITokenizer tokenizer) {
        this.tokenizer=tokenizer;
        
        this.indexPath=indexPath;
        File indexFile=new File(indexPath);
        
        if (!indexFile.exists()) {
            index=new Index();
        }else {
            Index index;
            try {
                index = serializeUtil.deserialize(indexPath);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("read index file error : "+indexPath);
            }
            this.index=index;
        }
    }
    
    /**
     * 添加索引串行执行
     * @throws IOException 
     * @createTime：2017年3月27日 
     * @author: gaojiabao
     */
    public synchronized void add(List<Doc> docs) throws IOException{
        for (Doc doc : docs) {
            add2RAM(doc);
        }
        System.out.println("正在持久化索引到磁盘...");
        serializeUtil.serialize(this.index, indexPath);
        System.out.println("持久化索引到磁盘完成...");
    }
    
    public synchronized void add(Doc doc) throws IOException{
        add2RAM(doc);
        serializeUtil.serialize(this.index, indexPath);
    }
    
    private synchronized void add2RAM(Doc doc)  throws IOException{
        System.out.println(Runtime.getRuntime().freeMemory()/1000/1000);
        long id=index.docIdMap.add(doc);
        
        List<String> lines=FileUtils.readLines(new File(doc.filePath));
        for (String line : lines) {
            List<String> tokens=tokenizer.getTokens(line);
            for (String token : tokens) {
                if (!index.containsKey(token)) {
                    TreeSet<Long> docSet=new TreeSet<>();
                    docSet.add(id);
                    index.put(token, docSet);
                }else {
                    index.get(token).add(id);
                }
            }
        }
        System.out.println("已索引文档： "+doc.filePath);
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
        /**
         * 删除内存索引文件
         */
        this.index=new Index();
        
        /**
         * 删除磁盘索引文件
         */
        File f=new File(indexPath);
        if (f.exists()&&f.isFile()) {
            f.delete();
        }
    }
    
}
