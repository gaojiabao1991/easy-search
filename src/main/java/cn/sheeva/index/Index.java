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
    
    public void serialize(){
        invertIndex.serialize();
        docMap.serialize();
    }
    
    public void clear(){
        invertIndex.clear();
        docMap.clear();
    }
    
    public Index copy(){
        return new Index(this.indexdir,this.indexname);
    }
}
