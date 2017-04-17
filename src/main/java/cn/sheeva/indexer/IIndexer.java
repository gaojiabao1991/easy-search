package cn.sheeva.indexer;

import java.io.IOException;
import java.util.List;

import cn.sheeva.doc.Doc;

public interface IIndexer {
    public void add(List<Doc> docs) throws Exception;
    
    public void add(Doc doc) throws Exception;
    
    public void delete(Doc doc) throws Exception;
    
    public void update(Doc doc) throws Exception;
}
