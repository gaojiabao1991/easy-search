package cn.sheeva.index.invertindex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import cn.sheeva.index.compress.InvertIndexCompresser;
import cn.sheeva.util.ASCII;
import cn.sheeva.util.LogUtil;

public abstract class InvertIndex<T>{
    private String path;
    private TreeMap<T, TreeSet<Long>> ram=new TreeMap<>();
    
    private Converter converter=new Converter();
    
    public InvertIndex(String path) {
        this.path=path;
    }
    
    /**
     * 将String转化为类型T
     * @createTime：2017年4月17日 
     * @author: gaojiabao
     */
    protected abstract T parseT(String word);
    
    /**
     * 将类型T转化为String
     * @createTime：2017年4月17日 
     * @author: gaojiabao
     */
    protected abstract String word2String(T word);
    
    /**
     * @return: <br>
     * word1 > word2 :return >0 <br>
     * word1 = word2 :return =0 <br>
     * word1 < word2 :return <0 <br>
     * @createTime：2017年4月17日 
     * @author: gaojiabao
     */
    protected abstract int compareTo(T word1, T word2);
    
    public TreeSet<Long> get(T word){
        TreeSet<Long> ramSet=getFromRam(word);
        List<Long> diskSet=getFromDisk(word);
        
        TreeSet<Long> union=new TreeSet<>();
        if (ramSet!=null) {
            union.addAll(ramSet);
        }
        if (diskSet!=null) {
            union.addAll(diskSet);
        }
        return union;
    }
    
    public TreeSet<Long> getFromRam(T word){
        return ram.get(word);
    }
    
//    private List<Long> getFromDisk(T word){
//        File f=new File(path);
//        if (f.exists()) {
//            try {
//                BufferedReader reader=new BufferedReader(new FileReader(f));
//                try {
//                    String line;
//                    while ((line=reader.readLine())!=null) {
//                        T diskWord=converter.convertLine2Word(line);
//                        
//                        int c=this.compareTo(diskWord, word);
//                        if (c==0) {
//                            return converter.convertLine2DocIds(line);
//                        }else if (c>0) {
//                            break;
//                        }
//                    }
//                }catch(Exception e){throw e;}
//                finally {
//                    try {
//                        reader.close();
//                    } catch (IOException e) {throw e;}
//                }
//            } catch (Exception e) {
//                LogUtil.err("read file error: "+path, e);
//            }
//        }
//        return null;
//    }
    
    private List<Long> getFromDisk(T word){
        File f=new File(path);
        if (f.exists()) {
            FileInputStream fis=null;
            FileChannel fc=null;
            try {
                fis = new FileInputStream(f);
                fc = fis.getChannel();
                MappedByteBuffer mbb = fc.map(MapMode.READ_ONLY, 0, f.length());
                String matchLine=this.binaryFindFromDisk(mbb, 0, mbb.limit(), word);
                if (matchLine!=null) {
                    matchLine=matchLine.replaceAll("\r\n", "");   //去掉返回行中的换行符
                    return converter.convertLine2DocIds(matchLine);
                }
                
            } catch (Exception e) {
                LogUtil.err("read from disk fail: "+word, e);
            }finally{
                try {
                    if (fis!=null) {
                        fis.close();
                    }
                    if (fc!=null) {
                        fc.close();
                    }
                } catch (Exception e) {
                    LogUtil.err("release resouce fail: "+word, e);
                }
            }
           
            
            
            
        }
        return null;
    }
    
    /**
     * 用二分查找从磁盘中返回匹配word的行，找不到返回null
     * @createTime：2017年4月20日 
     * @author: gaojiabao
     */
    public String binaryFindFromDisk(MappedByteBuffer mbb,int start,int end,T word){
        int mid=(start+end)/2;
        Line midLine=getLine(mbb, mid, start, end);
        if (midLine==null) {
            return null;
        }else {
//            String midWord=midLine.line.split("\t")[0];
            T midWord=converter.convertLine2Word(midLine.line);
            
            int cmp=this.compareTo(word, midWord);
            if (cmp==0) {
                return midLine.line;
            }else if (cmp>0) {
                return binaryFindFromDisk(mbb, midLine.end, end, word);
            }else {
                return binaryFindFromDisk(mbb, start, midLine.start, word);
            }
        }
        
    }
    
    /**
     * 在start~end范围内查找index所在的行并返回行内容及行首行尾下标 <br>
     * 示例：<br>
     *  001 0 <br>
        002 1 <br>
        003 2 <br>
     * 根据index分别向前和向后查找到第一个换行符，两个换行符中间的部分就是获取到的行
     * 如果index正好落在一个换行符上，则将这个换行符作为行尾的换行符  
     * @createTime：2017年4月19日 
     * @author: gaojiabao
     */
    private Line getLine(MappedByteBuffer mbb, int index, int start, int end) {
        if (start>=end) {
            return null;
        }
        int indexl=index;
        //定位到左边的第一个换行
        boolean findl=false;
        while (indexl-1>=start-1) {
            indexl--;
            if (indexl==-1) { //到达了行首，相当于找到了换行
                findl=true;
                break;
            }
            byte b=mbb.get(indexl);
            if (b==ASCII.ENTER) {
                findl=true;
                break;
            }
        }
        if (!findl) {
            return null;
        }
        
        //定位到右边的第一个换行
        int indexr=index;
        boolean findr=false;
        while(indexr<=end){
            byte b=mbb.get(indexr);
            if (b==ASCII.ENTER) {
                findr=true;
                break;
            }
            indexr++;
        }
        if (!findr) {
            return null;
        }
        
        int lineStart=indexl+1;
        int lineEnd=indexr+1;
        
        byte[] arr=new byte[lineEnd-lineStart];
        mbb.position(lineStart);
        mbb.get(arr);
        return new Line(new String(arr), lineStart, lineEnd);
    }
    
    public synchronized void put(T word,TreeSet<Long> docIds){
        ram.put(word, docIds);
    }
    
    public boolean containsFromRam(T word){
        return ram.containsKey(word);
    }
    
    public synchronized void merge(){
        File f=new File(path);
        if (f.exists()) {
            String outPath=path+".temp";
            File outFile=new File(outPath);
            
            TreeMap<T, TreeSet<Long>> ramSnapshot=null;
            ramSnapshot=ram;
            ram=new TreeMap<>();
            
            BufferedReader reader=null;
            PrintWriter writer=null;
            try {
                Iterator<Entry<T, TreeSet<Long>>> ramIterator= ramSnapshot.entrySet().iterator();
                reader=new BufferedReader(new FileReader(f));
                writer=new PrintWriter(new BufferedWriter(new FileWriter(outFile)));
                
                Entry<T, TreeSet<Long>> entry=ramIterator.hasNext()?ramIterator.next():null;
                String line=reader.readLine();
                
                while (entry!=null&&line!=null) {
                    T ramWord=entry.getKey();
                    T diskWord=converter.convertLine2Word(line);
                    String out="";

                    int c=this.compareTo(ramWord, diskWord);
                    if (c==0) {
                        TreeSet<Long> ramDocIds=entry.getValue();
                        List<Long> diskDocIds=converter.convertLine2DocIds(line);
                        TreeSet<Long> union=new TreeSet<>(ramDocIds);
                        union.addAll(diskDocIds);
                        out=converter.convertIndex2Line(ramWord, union);
                        
                        entry=ramIterator.hasNext()?ramIterator.next():null;
                        line=reader.readLine();
                    }else if (c<0) {
                        out=converter.convertIndex2Line(ramWord, entry.getValue());
                        entry=ramIterator.hasNext()?ramIterator.next():null;
                    }else {
                        out=line;
                        line=reader.readLine();
                    }
                    
                    writer.println(out);
                }
                
                while (ramIterator.hasNext()) {
                    entry=ramIterator.next();
                    String out=converter.convertIndex2Line(entry.getKey(), entry.getValue());
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
            TreeMap<T, TreeSet<Long>> ramSnapshot=ram;
            ram=new TreeMap<>();
            PrintWriter writer=null;
            try {
                writer=new PrintWriter(new BufferedWriter(new FileWriter(f)));
                for (Entry<T, TreeSet<Long>> entry : ramSnapshot.entrySet()) {
                    String line=converter.convertIndex2Line(entry.getKey(), entry.getValue());
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
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public InvertIndex copy(){
//        InvertIndex copy=new InvertIndex(this.path);
        InvertIndex<T> copy;
        try {
            copy = this.getClass().getDeclaredConstructor(String.class).newInstance(this.path);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("copy InvertIndex fail");
        }
        copy.ram=new TreeMap<>(this.ram);
        return copy;
    }
    
    private static class Line{
        public String line;
        public int start;   //行第一个字符的下标
        public int end;   //行最后一个字符的下标+1
        public Line(String line, int start, int end) {
            this.line = line;
            this.start = start;
            this.end = end;
        }
    }
    
    public class Converter{
        private final String separator1="\t";
        private final String separator2=",";
        
        private String convertIndex2Line(T word,Collection<Long> docIdsSet){
            StringBuffer line=new StringBuffer();
            line.append(word2String(word));
            line.append(separator1);

            List<Long> compressed=InvertIndexCompresser.getDocIdsDeltas(docIdsSet);
            List<String> deltas=new LinkedList<>();
            for (Long delta : compressed) {
                deltas.add(delta.toString());
            }
            line.append(String.join(separator2, deltas));
            return line.toString();
        }
        
        private T convertLine2Word(String line){
            String[] a=line.split(separator1);
            return parseT(a[0]);
        }
        
        private List<Long> convertLine2DocIds(String line){
          try {
              String[] a=line.split(separator1);
              
              List<Long> deltasList=new LinkedList<>();
              String[] deltas=a[1].split(separator2);
              for (String delta : deltas) {
                  deltasList.add(Long.parseLong(delta));
              }
              
              List<Long> docIds=InvertIndexCompresser.getDocIds(deltasList);
              return docIds;
          } catch (Exception e) {
              System.out.println("ERROR: "+line);
              e.printStackTrace();
          }
          return null;
      }
    }
}
