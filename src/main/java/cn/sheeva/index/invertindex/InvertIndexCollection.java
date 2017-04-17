package cn.sheeva.index.invertindex;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cn.sheeva.doc.field.Field;
import cn.sheeva.doc.field.def.FieldDef;
import cn.sheeva.doc.field.def.FieldType;

public class InvertIndexCollection {
    //<fieldname,invertindex>
    private Map<String, InvertIndex> invertIndexes=new HashMap<>();
    
    public InvertIndexCollection(String indexdir,String indexname,FieldDef... fieldDefs) {
        String rootPath=indexdir+"/"+indexname+"/";
        File rootDir=new File(rootPath);
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }
        
        for (FieldDef fieldDef : fieldDefs) {
            String fieldname=fieldDef.getFieldname();
            FieldType fieldType=fieldDef.getFieldtype();
            
           
            
            String invertIndexPath=rootPath+fieldname+".index";
            @SuppressWarnings("rawtypes")
            InvertIndex invertIndex;
            try {
                invertIndex=fieldType.getInvertIndexType().getDeclaredConstructor(String.class).newInstance(invertIndexPath);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("new invertIndex fail.");
            }
            invertIndexes.put(fieldname, invertIndex);
        }
    }
    
    public InvertIndexCollection(Map<String, InvertIndex> invertIndexes){
        this.invertIndexes=invertIndexes;
    }
    
    public void merge(){
        for (Entry<String, InvertIndex> entry : invertIndexes.entrySet()) {
            entry.getValue().merge();
        }
    }
    
    public void clear(){
        for (Entry<String, InvertIndex> entry : invertIndexes.entrySet()) {
            entry.getValue().clear();
        }
    }
    
//    public Map<String, InvertIndex> getInvertIndexes() {
//        return invertIndexes;
//    }
    
    public boolean contains(String fieldname){
        return invertIndexes.containsKey(fieldname);
    }
    
    public InvertIndex get(String fieldname){
        return invertIndexes.get(fieldname);
    }
    
    public InvertIndexCollection copy(){
        Map<String, InvertIndex> copyIndexes=new HashMap<>();
        for (Entry<String, InvertIndex> entry : invertIndexes.entrySet()) {
            copyIndexes.put(entry.getKey(), entry.getValue().copy());
        }
        return new InvertIndexCollection(copyIndexes);
    }
    
}
