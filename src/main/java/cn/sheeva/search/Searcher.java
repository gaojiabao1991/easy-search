package cn.sheeva.search;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import cn.sheeva.doc.Doc;
import cn.sheeva.doc.DocIdMap;
import cn.sheeva.index.Index;
import cn.sheeva.index.Indexer;

public class Searcher {
    /**
     * 搜索
     * @param index 倒排索引
     * @return 查询结果文档列表
     * 
     * @createTime：2017年3月28日 
     * @author: gaojiabao
     */
    public List<Doc> search(Index index,String word){
        TreeSet<Long> docIds=index.get(word);
        
        List<Doc> foundDocs=new LinkedList<>();
        for (Long docId : docIds) {
            foundDocs.add(index.docIdMap.get(docId));
        }
        
        return foundDocs;
    }
}
