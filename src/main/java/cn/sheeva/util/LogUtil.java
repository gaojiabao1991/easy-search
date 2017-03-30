package cn.sheeva.util;

public class LogUtil {
    public static void log(String info,Exception e){
        System.err.println("INFO: "+info);
        e.printStackTrace();
    }
}
