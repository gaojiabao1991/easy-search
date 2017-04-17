package cn.sheeva.indexer.fieldindexer.impl;

import cn.sheeva.doc.field.Field;
import cn.sheeva.index.invertindex.InvertIndex;
import cn.sheeva.indexer.fieldindexer.TokenFieldIndexer;
import cn.sheeva.search.token.ITokenizer;

public class TextFieldIndexer extends TokenFieldIndexer {

    public TextFieldIndexer(InvertIndex invertIndex, ITokenizer tokenizer) {
        super(invertIndex, tokenizer);
    }
}
