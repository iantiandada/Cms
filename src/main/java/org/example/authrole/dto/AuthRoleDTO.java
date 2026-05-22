package org.example.authrole.dto;

import lombok.Data;

import java.util.List;

@Data
public class AuthRoleDTO {
    private Integer roleId;
    private List<Integer> permissionIds;
}
