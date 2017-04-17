package cn.sheeva.indexer.fieldindexer;

import java.io.Serializable;
import java.util.List;
import java.util.TreeSet;

import cn.sheeva.doc.field.Field;
import cn.sheeva.index.invertindex.InvertIndex;
import cn.sheeva.search.token.ITokenizer;

public abstract class TokenFieldIndexer implements FieldIndexer {
    private InvertIndex fieldInvertIndex;
    private ITokenizer tokenizer;
    
    public TokenFieldIndexer(InvertIndex invertIndex,ITokenizer tokenizer) {
        this.fieldInvertIndex=invertIndex;
        this.tokenizer=tokenizer;
    }
    @Override
    public <T extends Serializable> void index(Field<T> field, Long id) {
        String value=field.getStringValue();
        List<String> tokens=tokenizer.getTokens(value);
        for (String token : tokens) {
            if (!fieldInvertIndex.containsFromRam(token)) {
                TreeSet<Long> docSet=new TreeSet<>();
                docSet.add(id);
                fieldInvertIndex.put(token, docSet);
            }else {
                fieldInvertIndex.getFromRam(token).add(id);
            }
        }
        
    }
}
