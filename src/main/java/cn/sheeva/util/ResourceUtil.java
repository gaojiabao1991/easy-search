package cn.sheeva.util;

public class ResourceUtil {
    public static String getResourcePath(String resource){
        return Thread.currentThread().getContextClassLoader().getResource(resource).getPath();
    }
}
