package cn.sheeva.doc.field;

import java.io.Serializable;

public abstract class Field <T extends Serializable> implements Serializable{
    private static final long serialVersionUID = 1L;
    
    protected String name;
    protected T value;
    
    public Field(String name, T value) {
        this.name=name;
        this.value=value;
    }
    
    public abstract String getStringValue();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
    
}
