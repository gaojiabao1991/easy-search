package cn.sheeva.indexer.fieldindexer.impl;

import cn.sheeva.doc.field.Field;
import cn.sheeva.index.invertindex.InvertIndex;
import cn.sheeva.indexer.fieldindexer.NormalFieldIndexer;
import cn.sheeva.search.token.ITokenizer;

public class IntFieldIndexer extends NormalFieldIndexer {

    public IntFieldIndexer(InvertIndex invertIndex, ITokenizer tokenizer) {
        super(invertIndex, tokenizer);
    }

}
