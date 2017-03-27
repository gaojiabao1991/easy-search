package cn.sheeva;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class ScanSearcher extends ASearcher {

    public ScanSearcher(String[] searchWords) {
        super(searchWords);
    }

    @Override
    public void search(String word) {
        List<String> foundFiles=new LinkedList<>();
        for(File f:dir.listFiles()){
            if (f.getName().endsWith(".txt")&&search(word,f)) {
                foundFiles.add(f.getName());
            }
        }
        showResult(word,foundFiles);
    }
    
    public boolean search(String word,File f){
        try {
            List<String> lines=FileUtils.readLines(f);
            for (String line : lines) {
                if (line.contains(word)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException("UnknownError,when search word: "+word+" from f: "+f.getName());
        }
    }
    
    public void showResult(String word,List<String> foundFiles){
        System.out.println("搜索词："+word+" ,找到文档 "+foundFiles.size()+" 篇，文档列表：");
        for (String s : foundFiles) {
            System.out.println(s);
        }
        System.out.println("------------------");
    }


}
