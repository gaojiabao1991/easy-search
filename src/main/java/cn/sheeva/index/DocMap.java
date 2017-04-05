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
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import cn.sheeva.doc.Doc;
import cn.sheeva.util.LogUtil;

public class DocMap{
    
    private String path;
    
    private Long nextId=0l;
    private TreeMap<Long, Doc> ram=new TreeMap<>();
    
    private final String separator="\t";
    
    public DocMap(String path) {
        this.path=path;
        getNextIdFromDisk(path);
    }
    
    public DocMap(String path,Long nextId){
        this.path=path;
        this.nextId=nextId;
    }
    
    private void getNextIdFromDisk(String path) {
        if (new File(path).exists()) {
            File f=new File(path);
            BufferedReader reader=null;
            try {
                reader=new BufferedReader(new FileReader(f));
                nextId=Long.parseLong(reader.readLine());
            } catch (Exception e) {
                LogUtil.err("read file fail: "+path, e);
            }finally {
                if (reader!=null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        LogUtil.err("release resource fail: "+path, e);
                    }
                }
            }
        }
    }
    
    public Doc get(long id){
//      return ram.get(id);
        if (ram.containsKey(id)) {
            return ram.get(id);
        }else {
            Doc doc= getFromDisk(id);
            if (doc==null) {
                throw new RuntimeException("get doc fail,docId: "+id);
            }
            return doc;
        }
        
    }
    
    private Doc getFromDisk(long id){
        File f=new File(path);
        if (f.exists()) {
            try {
                BufferedReader reader=new BufferedReader(new FileReader(f));
                try {
                    reader.readLine(); //skip first line: nextId
                    String line;
                    while ((line=reader.readLine())!=null) {
                        String[] a=line.split(separator);
                        long diskId=Long.parseLong(a[1]);
                        if (diskId==id) {
                            Doc doc=new Doc(a[2]);
                            doc.id=diskId;
                            return doc;
                        }else if (diskId>id) {
                            break;
                        }
                    }
                } catch (Exception e) {throw e;}
                finally {
                    try {
                        reader.close();
                    } catch (IOException e) {throw e;}                    
                }
            } catch (Exception e) {
                LogUtil.err("read file error: "+path, e);
            }
        }
        return null;
    }
    
    public synchronized void merge(){
        File f=new File(path);
        if (f.exists()) {
            String outPath=path+".temp";
            File outFile=new File(outPath);
            
            Map<Long, Doc> ramSnapshot=null;
            ramSnapshot=ram;
            ram=new TreeMap<>();
            
            BufferedReader reader=null;
            PrintWriter writer=null;
            try {
                reader=new BufferedReader(new FileReader(f));
                writer=new PrintWriter(new BufferedWriter(new FileWriter(outFile)));
                
                writer.println(nextId.toString());
                
                String line="";
                reader.readLine();//skip nextId
                while ((line=reader.readLine())!=null) {
                    writer.println(line);
                }
                
                for (Entry<Long, Doc> entry : ramSnapshot.entrySet()) {
                    String ramLine=this.convertEntry2line(entry);
                    writer.println(ramLine);
                }
            } catch (Exception e) {
                LogUtil.err("merge ram docmap and disk docmap fail.", e);
            }finally {
                try {
                    if (reader!=null) {
                        reader.close();
                    }
                    if (writer!=null) {
                        writer.close();
                    }
                } catch (Exception e2) {LogUtil.err("release resource fail.", e2);}               
            }
            
            f.delete();
            if (!outFile.renameTo(new File(path))) {
                throw new RuntimeException("rename temp file fail.");
            }
        }else {
            Map<Long, Doc> ramSnapshot=ram;
            ram=new TreeMap<>();
            PrintWriter writer=null;
            try {
                writer=new PrintWriter(new BufferedWriter(new FileWriter(f)));
                writer.println(nextId);
                for (Entry<Long, Doc> entry:ramSnapshot.entrySet()) {
                    String line=this.convertEntry2line(entry);
                    writer.println(line);
                }
            } 
            catch (Exception e) {
                LogUtil.err("sava ram docmap to disk fail", e);
            } 
            finally {
                if (writer!=null) {
                    writer.close();
                }
            }
        }
    }
    
    private String convertEntry2line(Entry<Long, Doc> entry){
        StringBuffer line=new StringBuffer();
        line.append(entry.getKey()).append(separator);
        line.append(entry.getValue().id).append(separator);
        line.append(entry.getValue().filePath);
        return line.toString();
    }
    
    public synchronized void clear(){
        nextId=0l;
        ram=new TreeMap<>();
        File f=new File(path);
        f.delete();
    }
    
    public synchronized long add(Doc doc){
        long id=nextId;
        doc.id=id;
        ram.put(id, doc);
        nextId++;
        return id;
    }
    
    public DocMap copy(){
        DocMap copy=new DocMap(this.path, this.nextId);
        copy.ram=new TreeMap<>(this.ram);
        return copy;
    }
    
}
