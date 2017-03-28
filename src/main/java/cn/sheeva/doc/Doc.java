package cn.sheeva.doc;

import java.io.File;
import java.io.Serializable;

public class Doc implements Serializable{
    private long id;
    public String filePath;
    
    public Doc(String filePath) {
        this.filePath=filePath;
    }
}
