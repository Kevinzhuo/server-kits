package com.huya.server.kits.serverkits.model;

/**
 * Created by Kevin[wangzhuo@yy.com] on 2017/9/12.
 */
public enum AreaEnum {

    DOMESTIC("domestic", 1),
    OVERSEA("oversea", 2);

    private String descri;

    private int value;

    AreaEnum(String descri, int value) {
        this.descri = descri;
        this.value = value;
    }

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
