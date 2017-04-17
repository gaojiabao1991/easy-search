package cn.sheeva.search.token;

import java.util.List;

public interface ITokenizer {
    public List<String> getTokens(String str);
}
