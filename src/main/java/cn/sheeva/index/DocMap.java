package cn.sheeva.index;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cn.sheeva.doc.Doc;
import cn.sheeva.util.LogUtil;

public class DocMap{
    
    private String path;
    
    private long nextId;
    private Map<Long, Doc> map=new HashMap<>();
    
    private final String separator="\t";
    
    public DocMap(String path) {
        this.path=path;
        
        if (new File(path).exists()) {
            deserialize();
        }
    }
    
    public void serialize(){
        File outFile=new File(path);
        try {
            PrintWriter writer=new PrintWriter(new BufferedWriter(new FileWriter(outFile)));
            try {
                writer.println(nextId);
                for (Entry<Long, Doc> entry:map.entrySet()) {
                    StringBuffer line=new StringBuffer();
                    
                    line.append(entry.getKey()).append(separator);
                    line.append(entry.getValue().id).append(separator);
                    line.append(entry.getValue().filePath);
                    
                    writer.println(line);
                }
            } 
            catch (Exception e) {throw e;} 
            finally {
                writer.close();
            }
        } catch (IOException e) {
            LogUtil.log("serialize fail： "+path, e);
        }
    }
    
    public void deserialize(){
        File f=new File(path);
        if (!f.exists()) {
            throw new RuntimeException("can't deserialize because file not exists: "+path);
        }
        try {
            BufferedReader reader=new BufferedReader(new FileReader(f));
            try {
                this.nextId=Long.parseLong(reader.readLine());
                String line;
                while ((line=reader.readLine())!=null) {
                    String[] a=line.split(separator);
                    Doc doc=new Doc(a[2]);
                    doc.id=Long.parseLong(a[1]);
                    map.put(Long.parseLong(a[0]), doc);
                }
            } catch (Exception e) {throw e;}
            finally {
                try {
                    reader.close();
                } catch (IOException e) {throw e;}
            }
        } catch (Exception e) {
            LogUtil.log("deserial fail： "+path, e);
        }
        
    }
    
    public void clear(){
        map=new HashMap<>();
        File f=new File(path);
        if (f.exists()&&f.isFile()) {
            f.delete();
        }
    }
    
    public synchronized long add(Doc doc){
        long id=nextId;
        doc.id=id;
        map.put(id, doc);
        nextId++;
        return id;
    }
    
    public Doc get(long id){
        return map.get(id);
    }
}
