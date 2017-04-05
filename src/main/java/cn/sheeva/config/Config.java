package cn.sheeva.config;

public class Config {
    //当jvm可用内存小于此阈值时,将自动触发hardCommit,单位是M
    public static final int autoCommitRamThreshold=100;
    
    
    
    
    /**
     * data configs
     */
    public static final String indexdir="E:/程序数据/easy-search/index";
    
    public static final String dataPath="E:/程序数据/easy-search/data/3000book";
    public static final String indexname="3000book";
    
//    public static final String dataPath="E:/程序数据/easy-search/data/zhangAiLing";
//    public static final String indexname="zhangAiLing";
    
//    public static final String dataPath="E:/程序数据/easy-search/data/500book";
//    public static final String indexname="500book";
    
}
