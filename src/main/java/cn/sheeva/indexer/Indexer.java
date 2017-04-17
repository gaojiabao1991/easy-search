package cn.sheeva.indexer;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import cn.sheeva.config.Config;
import cn.sheeva.doc.Doc;
import cn.sheeva.doc.field.Field;
import cn.sheeva.doc.field.def.FieldDef;
import cn.sheeva.index.Index;
import cn.sheeva.index.invertindex.InvertIndex;
import cn.sheeva.indexer.fieldindexer.FieldIndexer;
import cn.sheeva.search.token.ITokenizer;
import cn.sheeva.util.LogUtil;
import cn.sheeva.util.SerializeUtil;

public class Indexer implements IIndexer,Serializable{
    private static final long serialVersionUID = 1L;

    private ITokenizer tokenizer;
    public Index index;
    
    private HashMap<String, FieldIndexer> fieldIndexers=new HashMap<>();
    
    /**
     * 同一个indexName同一时间只能只能打开一个Indexer对象，否则可能会造成索引损坏
     * TODO 添加一个文件锁强制拒绝多个Indexer打开同一个索引
     */
    public Indexer(String indexdir,String indexname,ITokenizer tokenizer,FieldDef... fieldDefs) {
        this.tokenizer=tokenizer;
        this.index=new Index(indexdir, indexname, fieldDefs);
        
        for (FieldDef fieldDef : fieldDefs) {
            String fieldName=fieldDef.getFieldname();
            InvertIndex fieldIndex=index.invertIndexCollection.get(fieldName);
//            FieldIndexer fieldIndexer=(FieldIndexer) field.getIndexerType().getDeclaredConstructor(InvertIndex.class,ITokenizer.class).newInstance(fieldIndex,tokenizer);
            FieldIndexer fieldIndexer=null;
            try {
                fieldIndexer = (FieldIndexer) fieldDef.getFieldtype().getFieldIndexerType().getDeclaredConstructor(InvertIndex.class,ITokenizer.class).newInstance(fieldIndex,tokenizer);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("new FieldIndexer fail: "+fieldName);
            }
            fieldIndexers.put(fieldName, fieldIndexer);
        }
        
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
        checkDocFields(doc);
        
        for (Entry<String, Field> entry : doc.getFields().entrySet()) {
            String fieldname=entry.getKey();
            Field field=entry.getValue();
            
            FieldIndexer fieldIndexer=fieldIndexers.get(fieldname);
            fieldIndexer.index(field,id);
        }
        doc.clearFields();  //将内存中doc的fields清理掉，内存中的doc只保存id和filePath，fields保存在store文件里
        System.out.println("已索引文档："+doc.id);
        
    }

    private void checkDocFields(Doc doc) {
        for (Entry<String, Field> entry : doc.getFields().entrySet()) {
            String fieldname=entry.getKey();
            if (!fieldIndexers.containsKey(fieldname)) {
                throw new RuntimeException("doc field not define in index: "+fieldname);
            }
            
        }
    }
    
    public void hardCommit(){
        index.persist();
    }
    
    public synchronized void delete(Doc doc){
        
    }
    
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
