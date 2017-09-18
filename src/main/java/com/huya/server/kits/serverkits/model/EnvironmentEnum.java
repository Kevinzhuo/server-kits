package com.huya.server.kits.serverkits.model;

/**
 * Created by Kevin[wangzhuo@yy.com] on 2017/8/27.
 */
public enum EnvironmentEnum {
    PROD("prod", "production"),
    DEV("dev", "develop"),
    TEST("test", "test");

    private String env;

    private String descri;

    EnvironmentEnum(String env, String descri) {
        this.env = env;
        this.descri = descri;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
    }
}
