package cn.sheeva.doc.field.def;

import cn.sheeva.index.invertindex.InvertIndex;
import cn.sheeva.index.invertindex.impl.IntInvertIndex;
import cn.sheeva.index.invertindex.impl.StringInvertIndex;
import cn.sheeva.index.invertindex.impl.TextInvertIndex;
import cn.sheeva.indexer.fieldindexer.FieldIndexer;
import cn.sheeva.indexer.fieldindexer.impl.IntFieldIndexer;
import cn.sheeva.indexer.fieldindexer.impl.StringFieldIndexer;
import cn.sheeva.indexer.fieldindexer.impl.TextFieldIndexer;

public enum FieldType {
    IntFieldType(IntInvertIndex.class, IntFieldIndexer.class),
    StringFieldType(StringInvertIndex.class, StringFieldIndexer.class),
    TextFieldType(TextInvertIndex.class, TextFieldIndexer.class);
    
    private Class<? extends InvertIndex> invertIndexType;
    
    private Class<? extends FieldIndexer> fieldIndexerType;
    
    private FieldType(Class<? extends InvertIndex> invertIndexType,Class<? extends FieldIndexer> fieldIndexerType) {
        this.invertIndexType=invertIndexType;
        this.fieldIndexerType=fieldIndexerType;
    }
    
    public Class<? extends InvertIndex> getInvertIndexType() {
        return invertIndexType;
    }
    
    public Class<? extends FieldIndexer> getFieldIndexerType() {
        return fieldIndexerType;
    }
}
