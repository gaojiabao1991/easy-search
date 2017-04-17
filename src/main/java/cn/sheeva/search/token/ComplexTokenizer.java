package cn.sheeva.search.token;

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

public class ComplexTokenizer implements ITokenizer{
    private static JcsegTaskConfig config = new JcsegTaskConfig();
    private static ADictionary dic = DictionaryFactory.createSingletonDictionary(config);
    
    private ASegment seg;
    public ComplexTokenizer() {
        try {
            seg=(ASegment) SegmentFactory.createJcseg(JcsegTaskConfig.COMPLEX_MODE, new Object[] { config, dic });
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
        System.out.println(new ComplexTokenizer().getTokens("荀太太也在搜索枯肠，找没告诉过她的事。"));;
    }
}
