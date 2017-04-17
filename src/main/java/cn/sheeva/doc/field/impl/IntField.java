package cn.sheeva.doc.field.impl;

import cn.sheeva.doc.field.Field;

public class IntField extends Field<Integer>{

    private static final long serialVersionUID = 1L;

    public IntField(String fieldname, Integer value) {
        super(fieldname, value);
    }

    @Override
    public String getStringValue() {
        return super.value.toString();
    }


}
