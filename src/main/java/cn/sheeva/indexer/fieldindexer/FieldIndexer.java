package cn.sheeva.indexer.fieldindexer;

import java.io.Serializable;

import cn.sheeva.doc.field.Field;

public interface FieldIndexer{
    public <T extends Serializable> void index(Field<T> field,Long docId);
    
}
