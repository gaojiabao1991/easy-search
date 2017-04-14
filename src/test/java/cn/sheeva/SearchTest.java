package cn.sheeva;

import java.io.File;
import java.io.ObjectOutputStream.PutField;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.lionsoul.jcseg.util.StringUtil;

import cn.sheeva.doc.Doc;
import cn.sheeva.index.Indexer;
import cn.sheeva.search.Searcher;
import cn.sheeva.search.SearcherPool;
import cn.sheeva.search.query.booleanquery.BooleanQuery;
import cn.sheeva.search.query.termquery.TermQuery;
import cn.sheeva.token.SimpleTokenizer;
import cn.sheeva.util.TimeProfiler;

public class SearchTest {
    Indexer indexer=new Indexer(TestConfig.indexdir, TestConfig.indexname, new SimpleTokenizer());
    Searcher searcher=SearcherPool.getSearcher(indexer.index);

    
    @Test
    @Ignore
    public void termQueryTest(){
        TimeProfiler.begin();
        String word="搜索";
        
        TermQuery query=new TermQuery(word);
        List<Doc> foundDocs=searcher.search(query);

        long time=TimeProfiler.end();
        System.out.println("====================搜索结果列表：===========================");
        for (Doc doc : foundDocs) {
            System.out.println(new File(doc.filePath).getName());
        }
        System.out.println("============搜索词：‘"+word+"’, 搜索用时："+time+"ms("+time/1000+"s)"+",找到文档"+foundDocs.size()+" 篇===========");
    }
    
    @Test
    public void booleanQueryTest(){
        /**
         * 测试index数据：
         * 中国   1,2,2,2,2
                          人民  2,1,1,3
                          国旗  3,6,1
         */
        
        /**
         * 测试docMap数据：
         *  10
            1   1   1
            2   2   2
            3   3   3
            4   4   4
            5   5   5
            6   6   6
            7   7   7
            8   8   8
            9   9   9
            10  10  10
         */
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
        Map<String, String> testQuerys=new HashMap(){{
            put("中国 AND 人民", "3 7");
            put("中国 OR 人民", "1 2 3 4 5 7 9");
            put("NOT 人民", "1 5 6 8 9 10");
            put("中国 AND NOT 人民", "1 5 9");
            put("中国 OR 人民 AND 国旗", "3 9");
            put("中国 AND ( 人民 OR 国旗 )", "3 7 9");
            put("( 中国 AND ( 人民 OR 国旗 ) )", "3 7 9");
            put("( 中国 AND 人民 ) OR ( 人民 AND 国旗 )", "3 7");
            
        }};
        
        for (Entry<String, String> entry : testQuerys.entrySet()) {
            String q=entry.getKey();
            
            BooleanQuery query=new BooleanQuery(q);
            List<Doc> foundDocs=searcher.search(query);
            String r=StringUtils.join(foundDocs, " ");
            
            System.out.print("CHECK: "+entry.getKey());
            Assert.assertEquals(entry.getValue(), r);
            System.out.println("\tPASS: "+entry.getKey());
        }
        
    }
    
}
