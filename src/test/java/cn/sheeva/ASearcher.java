package cn.sheeva;

import java.io.File;
import java.util.List;

import cn.sheeva.config.Config;
import cn.sheeva.util.ResourceUtil;
import cn.sheeva.util.TimeProfiler;

public abstract class ASearcher {
    protected File dir=new File(ResourceUtil.getResourcePath(Config.curConfigSet.getResource()));;
    
    public abstract void search(String word);
    
    public void showTimeProfiler(String[] searchWords){
        System.out.println("================Searcher: "+getClass().getSimpleName()+"=================");
        TimeProfiler.begin();
        for (String word : searchWords) {
            search(word);
        }
        long time=TimeProfiler.end();
        System.out.println(getClass().getSimpleName()+"搜索用时："+time+"ms, ("+time/1000+"s)"+"\n\n");
        
    }
    
    public void showResult(String word,List<String> foundFiles){
        System.out.println("搜索词："+word+" ,找到文档 "+foundFiles.size()+" 篇，文档列表：");
        for (String s : foundFiles) {
            System.out.println(s);
        }
        System.out.println("------------------");
    }
}
