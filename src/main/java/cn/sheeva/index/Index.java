package cn.sheeva.index;

import java.io.File;
import java.io.Serializable;

public class Index{
    private String invertIndexPath;
    private String docMapPath;
    
    
    public InvertIndex invertIndex;
    public DocMap docMap;
    
    public Index(String indexdir,String indexname) {
        invertIndexPath=indexdir+"/"+indexname+".index";
        docMapPath=indexdir+"/"+indexname+".docmap";
        
        invertIndex=new InvertIndex(invertIndexPath);
        docMap=new DocMap(docMapPath);
    }
    
    public void serialize(){
        invertIndex.serialize();
        docMap.serialize();
    }
    
    public void clear(){
        invertIndex.clear();
        docMap.clear();
    }
}
