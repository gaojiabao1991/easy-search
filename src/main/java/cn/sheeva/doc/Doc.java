package cn.sheeva.doc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cn.sheeva.doc.field.Field;

public class Doc implements Serializable{
    private static final long serialVersionUID = 1L;
    
    public long id;
    public String filePath;
    private Map<String, Field> fields=new HashMap<>();
    
    public Doc() {
    }

    public Doc(long id,String filePath){
        this.id=id;
        this.filePath=filePath;
    }

    @Override
    public String toString() {
        return filePath;
    }
    
    
    public Doc addField(Field field){
        fields.put(field.getName(), field);
        return this;
    }
    
    public Map<String, Field> getFields() {
        return fields;
    }
    
    public Field getField(String fieldname){
        return fields.get(fieldname);
    }
    
    public void clearFields(){
        this.fields=null;
    }
}
