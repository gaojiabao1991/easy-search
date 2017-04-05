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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import cn.sheeva.util.LogUtil;

import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class InvertIndex{
    private String path;
    private TreeMap<String, TreeSet<Long>> ram=new TreeMap<>();
    private final String separator1="\t";
    private final String separator2=",";
    
    public InvertIndex(String path) {
        this.path=path;
    }
    
    public TreeSet<Long> get(String word){
        TreeSet<Long> ramSet=getFromRam(word);
        TreeSet<Long> diskSet=getFromDisk(word);
        
        TreeSet<Long> union=new TreeSet<>();
        if (ramSet!=null) {
            union.addAll(ramSet);
        }
        if (diskSet!=null) {
            union.addAll(diskSet);
        }
        return union;
    }
    
    public TreeSet<Long> getFromRam(String word){
        return ram.get(word);
    }
    
    private TreeSet<Long> getFromDisk(String word){
        File f=new File(path);
        if (f.exists()) {
            try {
                BufferedReader reader=new BufferedReader(new FileReader(f));
                try {
                    String line;
                    while ((line=reader.readLine())!=null) {
                        String[] a=line.split(separator1);
                        String diskWord=a[0];
                        
                        int c=diskWord.compareTo(word);
                        if (c==0) {
                            return this.convertLine2DocIds(line);
                        }else if (c>0) {
                            break;
                        }
                    }
                }catch(Exception e){throw e;}
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
    
    public synchronized void put(String word,TreeSet<Long> docIds){
        ram.put(word, docIds);
    }
    
    public boolean containsFromRam(String word){
        return ram.containsKey(word);
    }
    
    private TreeSet<Long> convertLine2DocIds(String line){
        String[] a=line.split(separator1);
        
        TreeSet<Long> docIdsSet=new TreeSet<>();
        String[] docIds=a[1].split(separator2);
        for (String docId : docIds) {
            docIdsSet.add(Long.parseLong(docId));
        }
        
        return docIdsSet;
    }
    
    private String convertIndex2Line(String word,TreeSet<Long> docIdsSet){
        StringBuffer line=new StringBuffer();
        line.append(word);
        line.append(separator1);
        
        List<String> docIds=new LinkedList<>();
        for (Long docId : docIdsSet) {
            docIds.add(docId.toString());
        }
        line.append(String.join(separator2, docIds));
        return line.toString();
    }
    
    
    public synchronized void merge(){
        File f=new File(path);
        if (f.exists()) {
            String outPath=path+".temp";
            File outFile=new File(outPath);
            
            TreeMap<String, TreeSet<Long>> ramSnapshot=null;
            ramSnapshot=ram;
            ram=new TreeMap<>();
            
            BufferedReader reader=null;
            PrintWriter writer=null;
            try {
                Iterator<Entry<String, TreeSet<Long>>> ramIterator= ramSnapshot.entrySet().iterator();
                reader=new BufferedReader(new FileReader(f));
                writer=new PrintWriter(new BufferedWriter(new FileWriter(outFile)));
                
                Entry<String, TreeSet<Long>> entry=ramIterator.hasNext()?ramIterator.next():null;
                String line=reader.readLine();
                
                while (entry!=null&&line!=null) {
                    String ramWord=entry.getKey();
                    String diskWord=line.split(separator1)[0];
                    String out="";

                    int c=ramWord.compareTo(diskWord);
                    if (c==0) {
                        TreeSet<Long> ramDocIds=entry.getValue();
                        TreeSet<Long> diskDocIds=this.convertLine2DocIds(line);
                        TreeSet<Long> union=new TreeSet<>(ramDocIds);
                        union.addAll(diskDocIds);
                        out=this.convertIndex2Line(ramWord, union);
                        
                        entry=ramIterator.hasNext()?ramIterator.next():null;
                        line=reader.readLine();
                    }else if (c<0) {
                        out=this.convertIndex2Line(ramWord, entry.getValue());
                        entry=ramIterator.hasNext()?ramIterator.next():null;
                    }else {
                        out=this.convertIndex2Line(diskWord, this.convertLine2DocIds(line));
                        line=reader.readLine();
                    }
                    
                    writer.println(out);
                }
                
                while (ramIterator.hasNext()) {
                    entry=ramIterator.next();
                    String out=this.convertIndex2Line(entry.getKey(), entry.getValue());
                    writer.println(out);
                }
                
                while ((line=reader.readLine())!=null) {
                    writer.println(line);
                }
                
            } catch (Exception e) {
                LogUtil.err("merge ram index and disk index fail.", e);
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
            TreeMap<String, TreeSet<Long>> ramSnapshot=ram;
            ram=new TreeMap<>();
            PrintWriter writer=null;
            try {
                writer=new PrintWriter(new BufferedWriter(new FileWriter(f)));
                for (Entry<String, TreeSet<Long>> entry : ramSnapshot.entrySet()) {
                    String line=this.convertIndex2Line(entry.getKey(), entry.getValue());
                    writer.println(line);
                }
            } 
            catch (Exception e) {
                LogUtil.err("save ram index to disk fail.", e);
            } 
            finally {
                if (writer!=null) {
                    writer.close();
                }
            }
        }
    }
    
    public synchronized void clear(){
        ram=new TreeMap<>();
        File f=new File(path);
        f.delete();
    }
    
    public InvertIndex copy(){
        InvertIndex copy=new InvertIndex(this.path);
        copy.ram=new TreeMap<>(this.ram);
        return copy;
    }
}
