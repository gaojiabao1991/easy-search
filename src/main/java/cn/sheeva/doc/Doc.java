package cn.sheeva.doc;

import java.io.File;
import java.io.Serializable;

public class Doc{
    public long id;
    public String filePath;
    
    public Doc(long id,String filePath){
        this.id=id;
        this.filePath=filePath;
    }
    
    public Doc(String filePath) {
        this.filePath=filePath;
    }
    
}
