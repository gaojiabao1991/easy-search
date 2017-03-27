package cn.sheeva;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import cn.sheeva.doc.Doc;
import cn.sheeva.doc.DocIdMap;
import cn.sheeva.index.Indexer;
import cn.sheeva.index.InvertIndex;
import cn.sheeva.token.Tokenizer;

public class IndexSearcher extends ASearcher {
    private Indexer indexer=new Indexer(new Tokenizer());

    public IndexSearcher(String[] searchWords) {
        super(searchWords);
        index();
    }

    @Override
    public void search(String word) {
        search(word,indexer);
    }
    
    public void search(String word,Indexer indexer){
        InvertIndex invertIndex=indexer.index;
        DocIdMap docIdMap=indexer.docIdMap;
        
        TreeSet<Long> docIds=invertIndex.get(word);
        
        List<String> foundFiles=new LinkedList<>();
        for (Long docId : docIds) {
            foundFiles.add(docIdMap.get(docId).file.getName());
        }
        
        showResult(word, foundFiles);
    }
    
    public boolean index(){
        for (File file : dir.listFiles()) {
            Doc doc=new Doc(file);
            try {
                indexer.add(doc);
            } catch (IOException e) {
                System.err.println("add doc fail,doc: "+doc.file.getName());
                return false;
            }
        }
        return true;
    }

}
