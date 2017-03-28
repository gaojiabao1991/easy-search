package cn.sheeva.util;

import java.io.IOException;

public interface ISerializeUtil<T> {
    
    public void serialize(T obj,String path) throws Exception;
    
    public T deserialize(String path) throws Exception;
}
