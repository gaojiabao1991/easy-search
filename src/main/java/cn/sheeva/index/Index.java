package cn.sheeva.index;

import java.io.Serializable;
import java.util.TreeMap;
import java.util.TreeSet;

import cn.sheeva.doc.DocIdMap;

public class Index extends TreeMap<String, TreeSet<Long>> implements Serializable{
    private static final long serialVersionUID = 1L;
    public DocIdMap docIdMap=new DocIdMap();
}
