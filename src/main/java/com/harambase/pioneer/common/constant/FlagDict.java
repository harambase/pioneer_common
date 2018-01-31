package com.harambase.pioneer.common.constant;

public enum FlagDict {

    SUCCESS("操作成功", 2001),
    FAIL("操作失败", 2002),
    SYSTEM_ERROR("系统异常", 2003),
    DELETE_BLOCK("不可删除", 2004),
    USER_DISABLED("用户已禁用", 2005),
    ADVISE_DUPLICATE("辅导关系已确立", 2006),
    REPEAT("课程重复", 2007),
    UNMET_PREREQ("预选课程未完成", 2008),
    COURSE_DISABLED("课程禁止选课", 2009),
    MAX_CAPACITY("课程容量达到上限", 2010),
    TIME_CONFLICT("时间冲突", 2011),
    PIN_EXISTS("该时段识别码已生成", 2012);

    private String m;
    private int v;

    FlagDict(String m, int v) {
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
