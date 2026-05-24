package org.example.authrole.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.authrole.dto.AuthRoleDTO;
import org.example.authrole.entity.Permission;
import org.example.authrole.entity.Role;
import org.example.authrole.mapper.RoleMapper;
import org.example.authrole.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/role")
public class AuthController {

    @Autowired
    private RoleMapper roleMapper;

    @PostMapping("/authRole")
    @Transactional
    public String authRole(@RequestBody AuthRoleDTO authRoleDTO) {
        log.info("roleId：{}", authRoleDTO.getRoleId());
        log.info("权限ID：{}", authRoleDTO.getPermissionIds());
        roleMapper.deleteRolePermission(authRoleDTO.getRoleId());
        roleMapper.authRole(authRoleDTO);
        return "";
    }

    @GetMapping("/roleList")
    public List<Role> roleList() {
        return roleMapper.selectList(null);
    }
    @Autowired
    private AuthService authService;

    /**
     * 控制的职责就很纯粹（只是负责接收请求，调用service）
     * @return
     */
    @GetMapping("/getAllPermission")
    public List<Permission> getAllPermission() {
        return authService.getAllPermission();
    }

    @GetMapping("/getPermissionOfCurrentRole")
    public List<Integer> getPermissionOfCurrentRole(Integer roleId){
        return roleMapper.getPermissionOfCurrentRole(roleId);
    }
}
