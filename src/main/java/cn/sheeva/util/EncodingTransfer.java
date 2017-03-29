package cn.sheeva.util;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.CharSet;

public class EncodingTransfer {
    /**
     * 将一个文件夹下所有txt文件转码后生成到目标文件夹
     * 示例： EncodingTransfer.transfer("C:/Users/sheeva/Desktop/zhangAiLing", "C:/Users/sheeva/Desktop/z", "GBK", "UTF-8");
     * @createTime：2017年3月29日 
     * @author: gaojiabao
     */
    public static void transfer(String srcPath,String desPath,String srcEncoding,String desEncoding){
        File dir=new File(srcPath);
        if (!dir.exists()||!dir.isDirectory()) {
            throw new RuntimeException("arg srcPath must point to a dir");
        }
        
        Collection<File> files=FileUtils.listFiles(dir, new String[]{"txt"}, false);
        for (File file : files) {
            String newFilePath=desPath+"/"+file.getName();
            try {
                FileUtils.writeLines(new File(newFilePath), desEncoding, FileUtils.readLines(file, srcEncoding));
            } catch (IOException e) {
                System.err.println("transfer file fail: "+file.getName());
            }
        }
    }
    
}
