package cn.sheeva;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

import org.apache.commons.io.FileUtils;

import cn.sheeva.doc.DocIdMap;
import cn.sheeva.index.Indexer;
import cn.sheeva.index.Index;
import cn.sheeva.util.ResourceUtil;
import cn.sheeva.util.SerializeUtil;

public class CommonTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        File file=new File("E:/程序数据/easy-search/data/500book/《冰屋》.txt");
        BufferedReader reader=new BufferedReader(new FileReader(file));
        while (reader.ready()) {
            System.out.println(reader.readLine());
        }
        reader.close();
    }
}


