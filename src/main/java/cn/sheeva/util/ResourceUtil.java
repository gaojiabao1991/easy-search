package cn.sheeva.util;

public class ResourceUtil {
    /**
     * @param resource 相对于classpath的路径，用'/'做分隔符 
     * @createTime：2017年3月28日 
     * @author: gaojiabao
     */
    public static String getResourcePath(String resource){
        return Thread.currentThread().getContextClassLoader().getResource(resource).getPath();
    }
}
