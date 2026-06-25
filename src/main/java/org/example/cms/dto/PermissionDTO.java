package org.example.cms.dto;

import lombok.Data;

@Data
public class PermissionDTO {
    private String name;
    private String menuType;
    private Integer parentId;
}