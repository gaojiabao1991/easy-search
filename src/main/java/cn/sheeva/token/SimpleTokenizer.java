package cn.sheeva.token;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.lionsoul.jcseg.tokenizer.ASegment;
import org.lionsoul.jcseg.tokenizer.core.ADictionary;
import org.lionsoul.jcseg.tokenizer.core.DictionaryFactory;
import org.lionsoul.jcseg.tokenizer.core.IWord;
import org.lionsoul.jcseg.tokenizer.core.JcsegException;
import org.lionsoul.jcseg.tokenizer.core.JcsegTaskConfig;
import org.lionsoul.jcseg.tokenizer.core.SegmentFactory;

public class SimpleTokenizer implements ITokenizer{
    private static JcsegTaskConfig config = new JcsegTaskConfig();
    private static ADictionary dic = DictionaryFactory.createSingletonDictionary(config);
    
    private ASegment seg;
    public SimpleTokenizer() {
        try {
            seg=(ASegment) SegmentFactory.createJcseg(JcsegTaskConfig.SIMPLE_MODE, new Object[] { config, dic });
        } catch (JcsegException e) {
            throw new RuntimeException("Unknown exception when create Asegment");
        }
    }
    
    public List<String> getTokens(String str){
        List<String> tokens=new LinkedList<>();
        try {
            seg.reset(new StringReader(str));

            //获取分词结果
            IWord word = null;
            while ((word = seg.next()) != null) {
                tokens.add(word.getValue());
            }
            return tokens;
        } catch (Exception e) {
            throw new RuntimeException("Unknown exception when getTokens, str: "+str);
        }
    } 

    public static void main(String[] args) throws JcsegException, IOException {
        
    }
}
