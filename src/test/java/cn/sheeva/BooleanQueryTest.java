package cn.sheeva;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
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
import cn.sheeva.search.query.booleanquery.BooleanQuery;
import cn.sheeva.search.token.SimpleTokenizer;
import cn.sheeva.util.SerializeUtil;

public class BooleanQueryTest {
    String indexName="boolean-test";
    
    FieldDef[] fieldDefs=new FieldDef[]{new FieldDef("title", FieldType.StringFieldType),new FieldDef("content", FieldType.TextFieldType)};
    Indexer indexer=new Indexer(TestConfig.indexdir, this.indexName, new SimpleTokenizer(), fieldDefs);
    Searcher searcher=SearcherPool.getSearcher(indexer.index);
    SerializeUtil<Doc> serializeUtil=new SerializeUtil<>();

    @Test
    @Ignore
    public void index() throws IOException{
        Doc doc1=new Doc().addField(new StringField("title", "1")).addField(new TextField("content", "中国"));
        Doc doc2=new Doc().addField(new StringField("title", "2")).addField(new TextField("content", "人民"));
        Doc doc3=new Doc().addField(new StringField("title", "3")).addField(new TextField("content", "国旗"));
        Doc doc4=new Doc().addField(new StringField("title", "4")).addField(new TextField("content", "中国 人民"));
        Doc doc5=new Doc().addField(new StringField("title", "5")).addField(new TextField("content", "中国 国旗"));
        Doc doc6=new Doc().addField(new StringField("title", "6")).addField(new TextField("content", "中国 人民 国旗"));
        
        
        List<Doc> docs=new LinkedList<>();
        docs.add(doc1);
        docs.add(doc2);
        docs.add(doc3);
        docs.add(doc4);
        docs.add(doc5);
        docs.add(doc6);
        
        for (Doc doc : docs) {
            indexer.add(doc);
        }
        
        indexer.hardCommit();
        System.out.println("索引完成.");
        
    }
    
    @Test
    public void search() throws ClassNotFoundException, IOException{
        @SuppressWarnings({ "rawtypes", "unchecked" })
        Map<String, String> testQuerys=new HashMap(){{
            put("content:中国 AND content:人民", "4 6");
            put("content:中国 OR content:人民", "1 2 4 5 6");
            put("NOT content:人民", "1 3 5");
            put("content:中国 AND NOT content:人民", "1 5");
            put("content:中国 OR content:人民 AND content:国旗", "5 6");
            put("content:中国 AND ( content:人民 OR content:国旗 )", "4 5 6");
            put("( content:中国 AND ( content:人民 OR content:国旗 ) )", "4 5 6");
            put("( content:中国 AND content:人民 ) OR ( content:人民 AND content:国旗 )", "4 6");
            
            put("content:中国 AND ( content:人民 OR content:国旗 ) AND ( title:5 OR title:6 OR title:7 )", "5 6");
            
        }};
        
        for (Entry<String, String> entry : testQuerys.entrySet()) {
            String q=entry.getKey();
            
            BooleanQuery query=new BooleanQuery(q);
            List<Doc> foundDocs=searcher.search(query);
            List<String> titles=new LinkedList<>();
            
            for (Doc doc : foundDocs) {
                Doc storeDoc=serializeUtil.deserialize(doc.filePath);
                titles.add(storeDoc.getField("title").getStringValue());
            }
            
            
            String r=StringUtils.join(titles, " ");
            
            System.out.print("CHECK: "+entry.getKey());
            Assert.assertEquals(entry.getValue(), r);
            System.out.println("\tPASS: "+entry.getKey());
        }
    }
}
