package com.harambase.pioneer.common.constant;

public enum RoleConst {

    USER("ROLE_USER", 1),
    ADMIN("ROLE_ADMIN", 2),
    TEACH("ROLE_TEACH", 3),
    LOGISTIC("ROLE_LOGISTIC", 4),
    SYSTEM("ROLE_SYSTEM", 5),
    STUDENT("ROLE_STUDENT", 6),
    FACULTY("ROLE_FACULTY", 7),
    ADVISOR("ROLE_ADVISOR", 8);

    // 成员变量
    private String roleName;
    private int roleId;

    RoleConst(String roleName, int roleId) {
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
