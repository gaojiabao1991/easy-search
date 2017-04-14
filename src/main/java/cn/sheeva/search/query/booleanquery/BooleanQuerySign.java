package cn.sheeva.search.query.booleanquery;

public enum BooleanQuerySign{
    AND("AND"),
    OR("OR"),
    NOT("NOT"),
    LB("("),
    RB(")");
    
    private String sign;
    
    private BooleanQuerySign(String sign) {
        this.sign=sign;
    }

    public static boolean isSign(String expStr){
        for (BooleanQuerySign sign : BooleanQuerySign.values()) {
            if (sign.toString().equals(expStr)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return sign;
    }
    
}