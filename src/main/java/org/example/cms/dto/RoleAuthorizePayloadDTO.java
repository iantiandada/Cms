package org.example.cms.dto;

import java.util.List;

public class RoleAuthorizePayloadDTO {
    private Integer roleId;
    private List<Integer> permissionIds;

    // 必须提供 getter 和 setter，否则 Spring 无法绑定 JSON
    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }
    public List<Integer> getPermissionIds() { return permissionIds; }
    public void setPermissionIds(List<Integer> permissionIds) { this.permissionIds = permissionIds; }
}