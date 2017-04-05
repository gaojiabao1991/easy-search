package cn.sheeva.index;

public class Index{
    private String indexdir;
    private String indexname;
    
    public InvertIndex invertIndex;
    public DocMap docMap;
    
    public Index(String indexdir,String indexname) {
        this.indexdir=indexdir;
        this.indexname=indexname;
        
        String invertIndexPath=indexdir+"/"+indexname+".index";
        String docMapPath=indexdir+"/"+indexname+".docmap";
        
        invertIndex=new InvertIndex(invertIndexPath);
        docMap=new DocMap(docMapPath);
    }
    
    public Index(String indexdir,String indexname,InvertIndex invertIndex,DocMap docMap){
        this.indexdir=indexdir;
        this.indexname=indexname;
        this.invertIndex=invertIndex;
        this.docMap=docMap;
    }
    
    /**
     * 合并内存索引到磁盘
     * @createTime：2017年4月5日 
     * @author: gaojiabao
     */
    public void persist(){
        System.out.println("merge ram index to disk start...");
        invertIndex.merge();
        docMap.merge();
        long freeRam=Runtime.getRuntime().freeMemory()/1000/1000;
        System.out.println("merge ram index to disk complete, freeRam: "+freeRam);
    }
    
    public void clear(){
        invertIndex.clear();
        docMap.clear();
    }
    
    public Index regenerate(){
        Index copy= new Index(indexdir,indexname,invertIndex.copy(), docMap.copy());
        return copy;
    }
}
