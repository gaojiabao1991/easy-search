package cn.sheeva.doc.field.impl;

import cn.sheeva.doc.field.Field;

public class TextField extends Field<String> {

    private static final long serialVersionUID = 1L;

    public TextField(String fieldname, String value) {
        super(fieldname, value);
    }

    @Override
    public String getStringValue() {
        return super.value;
    }


}
