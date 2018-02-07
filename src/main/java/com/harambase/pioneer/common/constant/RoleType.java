package com.harambase.pioneer.common.constant;

public enum RoleType {

    USER("用户", 1),
    ADMIN("管理员", 2),
    TEACH("教务", 3),
    LOGISTIC("后勤", 4),
    SYSTEM("系统", 5),
    STUDENT("学生", 6),
    FACULTY("教师", 7),
    ADVISOR("导师", 8);


    // 成员变量
    private String roleName;
    private int roleId;

    RoleType(String roleName, int roleId) {
        this.roleName = roleName;
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
