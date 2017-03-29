package cn.sheeva.config;

public class Config {
    
    public static final ConfigSet curConfigSet=ConfigSet.zhangAiLingConfigs;
    

    public static enum ConfigSet {
        bookConfigs {
            private String resource = "articles";
            private String indexname = "articles_index";
            
            @Override
            public String getResource() {return resource;}

            @Override
            public String getIndexname() {return indexname;}
        },
        
        zhangAiLingConfigs {
            private String resource = "zhangAiLing";
            private String indexname = "zhangAiLing_index";
            
            @Override
            public String getResource() {return resource;}

            @Override
            public String getIndexname() {return indexname;}
        };

        public abstract String getResource();
        public abstract String getIndexname();
    }

    public static void main(String[] args) {
        System.out.println(ConfigSet.bookConfigs.getResource());
        System.out.println(ConfigSet.zhangAiLingConfigs.getResource());
    }

}
