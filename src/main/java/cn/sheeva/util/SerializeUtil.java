package cn.sheeva.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SerializeUtil<T> implements ISerializeUtil<T>{
    public void serialize(T obj,String path) throws IOException{
        OutputStream os=new FileOutputStream(new File(path));
        ObjectOutputStream oos=new ObjectOutputStream(os);
        oos.writeObject(obj);
        oos.close();
        os.close();
    }
    
    @SuppressWarnings("unchecked")
    public T deserialize(String path) throws IOException, ClassNotFoundException{
        InputStream is=new FileInputStream(new File(path));
        ObjectInputStream ois=new ObjectInputStream(is);
        T obj=(T) ois.readObject();
        ois.close();
        is.close();
        return obj;
    }
}
