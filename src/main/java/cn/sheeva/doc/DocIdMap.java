package cn.sheeva.doc;

import java.util.HashMap;
import java.util.Map;

public class DocIdMap{
    private long nextId;
    private Map<Long, Doc> map=new HashMap<>();
    
    public synchronized long add(Doc doc){
        long id=nextId;
        map.put(id, doc);
        nextId++;
        return id;
    }
    
    public Doc get(long id){
        return map.get(id);
    }
}
