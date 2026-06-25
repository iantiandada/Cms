package org.example.cms.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.cms.dto.RoleAuthorizePayloadDTO;
import org.example.cms.dto.RoleDTO;
import org.example.cms.entity.Permission;
import org.example.cms.entity.Role;
import org.example.cms.mapper.RoleMapper;
import org.example.cms.mapper.RolePermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @GetMapping("/getAllPermission")
    public List<Permission> getAllPermission(){
        // 1. 从数据库一次性查出所有数据（只有 1 次 SQL 请求）
        List<Permission> allPermissions = roleMapper.getAllPermission();
        // 2. 将所有数据按 parentId 分组，转成 Map 结构存储
        Map<Integer, List<Permission>> childrenMap = allPermissions.stream()
                .filter(p -> p.getParentId() != null)
                .collect(Collectors.groupingBy(Permission::getParentId));
        // 3. 为每个节点注入它们的子节点
        allPermissions.forEach(p -> p.setChildren(childrenMap.get(p.getId())));
        // 4. 过滤出根节点（比如 parentId = 0 的节点）返回给前端
        return allPermissions.stream()
                .filter(p -> p.getParentId() == 0)
                .collect(Collectors.toList());
    }

    @GetMapping("/roleList")
    public List<Role> roleList(){
        return roleMapper.selectList(null);
    }
    @GetMapping("/getPermissionCurrentRole")
    public List<Integer> getPermissionCurrentRole(Integer roleId){
        return roleMapper.getPermissionCurrentRole(roleId);
    }
    @PostMapping("/authRole")
    public String authRole(@RequestBody RoleAuthorizePayloadDTO payload) {
        log.info("保存角色权限：roleId={}, permissionIds={}", payload.getRoleId(), payload.getPermissionIds());
        // 1. 删除该角色所有旧权限
        rolePermissionMapper.deleteByRoleId(payload.getRoleId());
        // 2. 如果传了权限，批量插入新权限
        if (payload.getPermissionIds() != null && !payload.getPermissionIds().isEmpty()) {
            rolePermissionMapper.insertBatch(payload.getRoleId(), payload.getPermissionIds());
        }
        return "success";
    }


    @DeleteMapping("/deleteRole/{id}")
    public String deleteRole(@PathVariable Integer id) {
        roleMapper.deleteById(id);
        return "删除成功";
    }
    @PostMapping("/addRole")
    public String addRole(@RequestBody RoleDTO dto) {
        Role role = new Role();
        role.setName(dto.getRoleName());   // 将接收到的角色名设置到实体
        roleMapper.insert(role);           // MyBatis-Plus 内置的插入方法
        return "添加成功";
    }
}



