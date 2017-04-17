package cn.sheeva;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;

import cn.sheeva.doc.Doc;
import cn.sheeva.doc.field.def.FieldDef;
import cn.sheeva.doc.field.def.FieldType;
import cn.sheeva.doc.field.impl.StringField;
import cn.sheeva.doc.field.impl.TextField;
import cn.sheeva.indexer.Indexer;
import cn.sheeva.search.Searcher;
import cn.sheeva.search.SearcherPool;
import cn.sheeva.search.query.termquery.TermQuery;
import cn.sheeva.search.token.SimpleTokenizer;
import cn.sheeva.util.SerializeUtil;
import cn.sheeva.util.TimeProfiler;

public class TermQueryTest {
    FieldDef[] fieldDefs=new FieldDef[]{new FieldDef("title", FieldType.StringFieldType),new FieldDef("content", FieldType.TextFieldType)};
    Indexer indexer=new Indexer(TestConfig.indexdir, TestConfig.indexname, new SimpleTokenizer(), fieldDefs);
    Searcher searcher=SearcherPool.getSearcher(indexer.index);
    SerializeUtil<Doc> serializeUtil=new SerializeUtil<>();
    
    @Test
    @Ignore
    public void index() throws IOException{
        File dataDir=new File(TestConfig.dataPath);
        for (File f : dataDir.listFiles()) {
            Doc doc=new Doc();
            String title=f.getName().substring(0, f.getName().lastIndexOf("."));
            String content=FileUtils.readFileToString(f);
            
            doc.addField(new StringField("title", title)).addField(new TextField("content", content));
            
            indexer.add(doc);
        }
        indexer.hardCommit();
        System.out.println("索引完成");
    }
    
    @Test
  public void termQueryTest() throws ClassNotFoundException, IOException{
      TimeProfiler.begin();
      String word="搜索";
      
      TermQuery query=new TermQuery("content",word);
      List<Doc> foundDocs=searcher.search(query);

      long time=TimeProfiler.end();
      System.out.println("====================搜索结果列表：===========================");
      for (Doc doc : foundDocs) {
          Doc storeDoc=serializeUtil.deserialize(doc.filePath);
          System.out.println(storeDoc.getField("title").getValue());
      }
      System.out.println("============搜索词：‘"+word+"’, 搜索用时："+time+"ms("+time/1000+"s)"+",找到文档"+foundDocs.size()+" 篇===========");
  }
}
