package org.example.cms.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.cms.dto.PermissionDTO;
import org.example.cms.entity.MenuType;
import org.example.cms.entity.Permission;
import org.example.cms.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/permission")
public class PermissionController {
    @Autowired
    private PermissionMapper permissionMapper;
    @GetMapping("/getAllPermission")

    public List<Permission> getAllPermission(){
        return permissionMapper.getAllPermission();
    }
    @DeleteMapping("/deletePermission/{id}")
    @Transactional
    public String deletePermission(@PathVariable Long id) {
        try {
            log.info("收到删除请求，id = {}", id);
            int result = permissionMapper.deleteById(id);
            if (result > 0) {
                log.info("删除成功，id = {}", id);
                return "删除成功";
            } else {
                log.warn("删除失败，未找到对应记录，id = {}", id);
                return "删除失败：未找到对应记录";
            }
        } catch (Exception e) {
            log.error("删除异常，id = {}", id, e);
            throw new RuntimeException("删除失败：" + e.getMessage());
        }
    }

    @PostMapping("/addPermission")
    public String addPermission(@RequestBody PermissionDTO dto) {
        Permission permission = new Permission();
        permission.setName(dto.getName());
        permission.setMenuType(MenuType.valueOf(dto.getMenuType()));
        permission.setParentId(dto.getParentId());
        permissionMapper.insert(permission);
        return "添加成功";
    }
}
