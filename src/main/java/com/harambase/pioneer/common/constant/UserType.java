package com.harambase.pioneer.common.constant;

public enum UserType {

    STUDENT("s", 1),
    FACULTY("f", 2),
    ADMINISTRATOR("a", 3);

    // 成员变量
    private String m;
    private int v;

    UserType(String m, int v) {
        this.m = m;
        this.v = v;
    }

    public String getM() {
        return m;
    }

    public int getV() {
        return v;
    }
}
