package cn.sheeva.index;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import cn.sheeva.doc.field.Field;
import cn.sheeva.doc.field.def.FieldDef;
import cn.sheeva.index.invertindex.InvertIndexCollection;

public class Index{
    private String indexdir;
    private String indexname;
    
    @SuppressWarnings("rawtypes")
    public InvertIndexCollection invertIndexCollection;
    public DocMap docMap;
    
    public Index(String indexdir,String indexname,FieldDef... fieldDefs) {
        this.indexdir=indexdir;
        this.indexname=indexname;
        
        docMap=new DocMap(indexdir,indexname,true);
        
        invertIndexCollection=new InvertIndexCollection(indexdir, indexname, fieldDefs);
        
    }
    
    public Index(String indexdir,String indexname,InvertIndexCollection invertIndexCollection,DocMap docMap){
        this.indexdir=indexdir;
        this.indexname=indexname;
        this.invertIndexCollection=invertIndexCollection;
        this.docMap=docMap;
    }
    
    /**
     * 合并内存索引到磁盘
     * @createTime：2017年4月5日 
     * @author: gaojiabao
     */
    public void persist(){
        System.out.println("merge ram index to disk start...");
        invertIndexCollection.merge();
        docMap.merge();
        long freeRam=Runtime.getRuntime().freeMemory()/1000/1000;
        System.out.println("merge ram index to disk complete, freeRam: "+freeRam);
    }
    
    public void clear(){
        invertIndexCollection.clear();
        docMap.clear();
    }
    
    public Index copy(){
        Index copy= new Index(indexdir,indexname,invertIndexCollection.copy(), docMap.copy());
        return copy;
    }
}
