package cn.sheeva.indexer.fieldindexer;

import java.io.Serializable;
import java.util.TreeSet;

import cn.sheeva.doc.field.Field;
import cn.sheeva.index.invertindex.InvertIndex;
import cn.sheeva.search.token.ITokenizer;

public abstract class NormalFieldIndexer implements FieldIndexer {
    private InvertIndex fieldInvertIndex;
    private ITokenizer tokenizer;
    
    public NormalFieldIndexer(InvertIndex fieldInvertIndex,ITokenizer tokenizer) {
        this.fieldInvertIndex=fieldInvertIndex;
        this.tokenizer=tokenizer;
    }
    
    @Override
    public <T extends Serializable> void index(Field<T> field, Long id) {
//        String fieldname=field.getName();
        String value=field.getStringValue();
        if (!fieldInvertIndex.containsFromRam(value)) {
            TreeSet<Long> docSet=new TreeSet<>();
            docSet.add(id);
            fieldInvertIndex.put(value, docSet);
        }else {
            fieldInvertIndex.getFromRam(value).add(id);
        }
    }
}
