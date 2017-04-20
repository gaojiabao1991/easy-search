package cn.sheeva.searchdisk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class BinarySearchFile {
    private static final byte ENTER=10;
    private static final byte TAB=9;

    public boolean binaryFind(MappedByteBuffer mbb,int start,int end,String word){
        int mid=(start+end)/2;
        Line midLine=getLine(mbb, mid, start, end);
        if (midLine==null) {
            return false;
        }else {
            String midWord=midLine.line.split("\t")[0];
            System.out.println(midWord);
            
            int cmp=word.compareTo(midWord);
            if (cmp==0) {
                return true;
            }else if (cmp>0) {
                return binaryFind(mbb, midLine.end, end, word);
            }else {
                return binaryFind(mbb, start, midLine.start, word);
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
            if (b==ENTER) {
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
            if (b==ENTER) {
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
    
    @Test
    public void matchTest() throws IOException{
        File f = new File("E:/程序数据/easy-search/test/title.index");
        BufferedReader reader=new BufferedReader(new FileReader(f));
        String line;
        
        FileChannel fc = new FileInputStream(f).getChannel();
        MappedByteBuffer mbb = fc.map(MapMode.READ_ONLY, 0, f.length());
        while ((line=reader.readLine())!=null) {
            String word=line.split("\t")[0];
            System.out.println("Search: "+word);
            mbb.clear();
            Assert.assertEquals(true, binaryFind(mbb, 0, mbb.limit(), word));
        }
        reader.close();
        fc.close();
    }
    
    @Test
    public void notMatchTest() throws IOException{
        File f = new File("E:/程序数据/easy-search/test/title.index");
        String[] words=new String[]{"AAA","搜索","红楼"};
        FileChannel fc = new FileInputStream(f).getChannel();
        MappedByteBuffer mbb = fc.map(MapMode.READ_ONLY, 0, f.length());
        for (String word : words) {
            System.out.println("Search: "+word);
            mbb.clear();
            Assert.assertEquals(false, binaryFind(mbb, 0, mbb.limit(), word));
        }
        fc.close();
    }
    
}
