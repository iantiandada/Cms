package org.example.cms.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String password;
    private Integer roleId;      // 前端选中的角色ID
}