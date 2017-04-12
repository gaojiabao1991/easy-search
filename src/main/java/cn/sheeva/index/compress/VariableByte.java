package cn.sheeva.index.compress;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

@Deprecated
public class VariableByte {
    private List<Byte> bytes=new LinkedList<>();
    
    public VariableByte(long value){
        boolean firstTime=true;
        while (value>0) {
            Long mod=value%128;
            value =value/128;
            
            byte b;
            if (firstTime) {
                b= (byte) (mod.byteValue()+(byte)128);
            }else {
                b=mod.byteValue();
            }
            
            this.bytes.add(b);
            firstTime=false;
        }
        Collections.reverse(this.bytes);
    }
    
    public List<Byte> getBytes(){
        return bytes;
    }
    
    public String getVbString(){
        List<String> vbs=new LinkedList<>();
        for (Byte b : bytes) {
            Integer i=b&0xff;
            vbs.add(Integer.toBinaryString(i));
        }
        return StringUtils.join(vbs, " ");
    }
}
