package cn.sheeva.index;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.sound.sampled.Line;

import org.apache.commons.io.FileUtils;

import cn.sheeva.doc.Doc;
import cn.sheeva.util.LogUtil;
import cn.sheeva.util.SerializeUtil;
import cn.sheeva.util.SerializeXmlUtil;

public class DocMap{
    
    private String indexdir;
    private String indexname;
    
    private String filePath;
    private String storePath;
    
    private Long nextId=0l;
    private HashMap<Long, Doc> ram=new HashMap<>();
    
    private final String separator="\t";
    
    private SerializeUtil<Doc> serializeUtil=new SerializeUtil<>();
    
    public DocMap(String indexdir, String indexname ,boolean loadDisk) {
        this.indexdir=indexdir;
        this.indexname=indexname;
        
        String rootPath=indexdir+"/"+indexname+"/";
        File rootDir=new File(rootPath);
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }
        
        this.filePath=rootPath+"docmap";
        
        this.storePath=rootPath+"store/";
        
        File storeDir=new File(storePath);
        if (!storeDir.exists()) {
            storeDir.mkdirs();
        }
        
        File f=new File(filePath);
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
                LogUtil.err("read file error: "+filePath, e);
            }
        }
    }
    
    
    public Doc get(long id){
        return ram.get(id);
    }
    
    public Set<Long> getAllIds(){
        return ram.keySet();
    }
    
    
    public synchronized void merge(){
        File f=new File(filePath);
        String outPath=filePath+".temp";
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
        outFile.renameTo(new File(filePath));
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
        ram=new HashMap<>();
        File f=new File(filePath);
        f.delete();
    }
    
    public synchronized long add(Doc doc){
        long id=nextId;
        doc.id=id;
        doc.filePath=storePath+doc.id;
        
        writeStoreFile(doc);    //store file
                
        ram.put(id, doc);
        nextId++;
        return id;
    }


    private void writeStoreFile(Doc doc) {
        try {
            serializeUtil.serialize(doc, doc.filePath);
        } catch (IOException e) {
            LogUtil.err("write store file fail: "+doc.filePath, e);
        }
    }
    
    public DocMap copy(){
        DocMap copy=new DocMap(this.indexdir,this.indexname,false);
        copy.ram=new HashMap<>(this.ram);
        return copy;
    }
    
    public String getStorePath() {
        return storePath;
    }
    
}
