package cn.sheeva.index.compress;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class VariableByteTest {
    @Test
    public void test(){
        Long a=214577l;
        //110100011000110001
        //00001101 00001100 10110001
        Assert.assertEquals("1101 1100 10110001", new VariableByte(a).getVbString());
        
        Long b=824l;
        //1100111000
        //00000110 10111000
        Assert.assertEquals("110 10111000", new VariableByte(b).getVbString());
        
        List<Byte> bytesList=new VariableByte(a).getBytes();
        byte[] bytes=new byte[bytesList.size()];
        for (int i = 0; i < bytesList.size(); i++) {
            bytes[i]=bytesList.get(i);
        }
        
        String s=new String(bytes);
        System.out.println(s);
    }
}
