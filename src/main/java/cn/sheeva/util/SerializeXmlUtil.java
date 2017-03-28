package cn.sheeva.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerializeXmlUtil<T> implements ISerializeUtil<T> {
    public void serialize(T obj,String path) throws IOException{
        OutputStream os=new FileOutputStream(new File(path));
        XMLEncoder encoder=new XMLEncoder(os,"UTF-8",true,0);
        encoder.writeObject(obj);
        encoder.close();
        os.close();
    }
    
    @SuppressWarnings("unchecked")
    public T deserialize(String path) throws IOException, ClassNotFoundException{
        InputStream is=new FileInputStream(new File(path));
        XMLDecoder decoder=new XMLDecoder(is);
        T obj=(T) decoder.readObject();
        decoder.close();
        is.close();
        return obj;
    }
}
