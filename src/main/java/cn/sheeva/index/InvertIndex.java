package cn.sheeva.index;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import cn.sheeva.util.LogUtil;

import java.util.TreeMap;
import java.util.TreeSet;

public class InvertIndex{
    private String path;
    public TreeMap<String, TreeSet<Long>> map=new TreeMap<>();
    private final String separator1="\t";
    private final String separator2=",";

    public InvertIndex(String path) {
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
                for (Entry<String, TreeSet<Long>> entry : map.entrySet()) {
                    StringBuffer line=new StringBuffer();
                    line.append(entry.getKey());
                    line.append(separator1);
                    
                    List<String> docIds=new LinkedList<>();
                    for (Long docId : entry.getValue()) {
                        docIds.add(docId.toString());
                    }
                    
                    line.append(String.join(separator2, docIds));
                    
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
            throw new RuntimeException("can't deserialize from file because file not exists: "+path);
        }
        
        try {
            BufferedReader reader=new BufferedReader(new FileReader(f));
            try {
                String line;
                while ((line=reader.readLine())!=null) {
                    String[] a=line.split(separator1);
                    
                    String word=a[0];
                    String[] docIds=a[1].split(separator2);
                    
                    TreeSet<Long> docIdsSet=new TreeSet<>();
                    for (String docId : docIds) {
                        docIdsSet.add(Long.parseLong(docId));
                    }
                    
                    map.put(word, docIdsSet);
                }
            } catch (Exception e) {throw e;}
            finally {
                try {
                    reader.close();
                } catch (IOException e) {throw e;}
            }
        } catch (Exception e) {
            LogUtil.log("deserialize fail: "+path, e);
        }
    }
    
    public void clear(){
        map=new TreeMap<>();
        File f=new File(path);
        if (f.exists()&&f.isFile()) {
            f.delete();
        }
    }
}
