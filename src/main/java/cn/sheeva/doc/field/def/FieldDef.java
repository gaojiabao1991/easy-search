package cn.sheeva.doc.field.def;

import cn.sheeva.doc.field.Field;

public class FieldDef {
    private String fieldname;
    private FieldType fieldtype;
    
    public FieldDef(String fieldname, FieldType fieldtype) {
        this.fieldname = fieldname;
        this.fieldtype = fieldtype;
    }
    
    public String getFieldname() {
        return fieldname;
    }
    
    public FieldType getFieldtype() {
        return fieldtype;
    }
    
}
