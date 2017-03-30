package cn.sheeva;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CommonTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        File file=new File("E:/程序数据/easy-search/data/500book/《冰屋》.txt");
        BufferedReader reader=new BufferedReader(new FileReader(file));
        
        File outFile=new File("C:/test.txt");
        PrintWriter writer=new PrintWriter(new BufferedWriter(new FileWriter(outFile)));
        while (reader.ready()) {
            String line=reader.readLine();
            System.out.println(line);
            writer.println(line);
            
        }
        reader.close();
        writer.flush();
        writer.close();
    }
}


