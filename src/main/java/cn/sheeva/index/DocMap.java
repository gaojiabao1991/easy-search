package cn.sheeva.index;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
    
    public DocMap(String path,boolean loadDisk) {
        this.path=path;
        File f=new File(path);
        if (loadDisk&&f.exists()) {
            try {
                BufferedReader reader=new BufferedReader(new FileReader(f));
                try {
                    nextId=Long.parseLong(reader.readLine());
                    String line;
                    while ((line=reader.readLine())!=null) {
                        String[] a=line.split(separator);
                        long id=Long.parseLong(a[0]);
                        String docPath=a[2];
                        ram.put(id, new Doc(id, docPath));
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
    }
    
    
    public Doc get(long id){
        return ram.get(id);
    }
    
    
    public synchronized void merge(){
        File f=new File(path);
        String outPath=path+".temp";
        File outFile=new File(outPath);
        
        Map<Long, Doc> ramSnapshot=null;
        ramSnapshot=ram;
        
        PrintWriter writer=null;
        try {
            writer=new PrintWriter(new BufferedWriter(new FileWriter(outFile)));
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
        
        f.delete();
        outFile.renameTo(new File(path));
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
        DocMap copy=new DocMap(this.path,false);
        copy.ram=new TreeMap<>(this.ram);
        return copy;
    }
    
}
