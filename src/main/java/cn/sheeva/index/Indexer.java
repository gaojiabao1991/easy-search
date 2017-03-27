package cn.sheeva.index;

import java.io.IOException;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;

import cn.sheeva.doc.Doc;
import cn.sheeva.doc.DocIdMap;
import cn.sheeva.token.Tokenizer;

/**
 * 该类实例是线程安全的
 * @author sheeva
 *
 */
public class Indexer {
    private Tokenizer tokenizer;
    public InvertIndex index=new InvertIndex();
    public DocIdMap docIdMap=new DocIdMap();
    
    public Indexer(Tokenizer tokenizer) {
        this.tokenizer=tokenizer;
    }
    
    /**
     * 添加索引串行执行
     * @throws IOException 
     * @createTime：2017年3月27日 
     * @author: gaojiabao
     */
    public synchronized void add(List<Doc> docs) throws IOException{
        for (Doc doc : docs) {
            add(doc);
        }
    }
    
    public synchronized void add(Doc doc) throws IOException{
        long id=docIdMap.add(doc);
        
        List<String> lines=FileUtils.readLines(doc.file);
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
    }
    
//    public void delete(List<Doc> docs){
//        
//    }
    
    public void delete(Doc doc){
        
    }
    
//    public void update(List<Doc> docs){
//        
//    }
    
    public void update(Doc doc){
        
    }
}
