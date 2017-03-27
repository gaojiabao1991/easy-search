package cn.sheeva;

import java.io.File;

import cn.sheeva.util.ResourceUtil;
import cn.sheeva.util.TimeProfiler;

public abstract class ASearcher {
    protected String[] searchWords;
    protected File dir;
    
    public ASearcher(String[] searchWords) {
        this.searchWords=searchWords;
        this.dir=new File(ResourceUtil.getResourcePath("articles"));
    }
    
    public abstract void search(String word);
    
    public void showTimeProfiler(){
        System.out.println("================Searcher: "+getClass().getSimpleName()+"=================");
        TimeProfiler.begin();
        for (String word : searchWords) {
            search(word);
        }
        System.out.println(getClass().getSimpleName()+"搜索用时："+TimeProfiler.end()+"\n\n");
        
    }
}
