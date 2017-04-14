package cn.sheeva.search.query;

import java.util.List;

import cn.sheeva.doc.Doc;
import cn.sheeva.index.Index;

public interface IQuery {
    public List<Doc> query(Index index);
}
