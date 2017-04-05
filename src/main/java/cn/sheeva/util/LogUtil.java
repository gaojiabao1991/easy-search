package cn.sheeva.util;

public class LogUtil {
    public static void err(String err,Exception e){
        System.err.println("ERR: "+err);
        e.printStackTrace();
    }
    
    public static void err(String err){
        System.err.println("ERR: "+err);
    }
    
    public static void info(String info){
        System.out.println("INFO: "+info);
    }
}
