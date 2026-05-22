package org.example.authrole.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.authrole.dto.AuthRoleDTO;
import org.example.authrole.entity.Permission;
import org.example.authrole.entity.Role;
import org.example.authrole.mapper.RoleMapper;
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
        // SELECT * FROM role
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        return roleMapper.selectList(null);
    }

    @GetMapping("/getAllPermission")
    public List<Permission> getAllPermission() {
        // 如果只是获取所有权限，很简单，但是还要设置一级菜单、二级菜单、三级菜单的关系
        // 解决方案（2个）
        // 1、用之前的递归方式（已经会了：1月份用了1次；CMS中也用了递归）
        // 2、查询方式（会有N+1问题）：这是MyBatis的知识点
        // 3、不用递归，一次性抓取所有菜单，再设置父子关系（不用递归）

        // 1. 从数据库一次性查出所有数据（只有 1 次 SQL 请求），allPermissions（包含19个）
        List<Permission> allPermissions = roleMapper.getAllPermission();

        // 2. 将所有数据按 parentId 分组，转成 Map 结构存储
        Map<Integer, List<Permission>> childrenMap = allPermissions.stream()
                .filter(p -> p.getParentId() != null)
                .collect(Collectors.groupingBy(Permission::getParentId));

        // 3. 为每个节点注入它们的子节点
        allPermissions.forEach(p -> p.setChildren(childrenMap.get(p.getId())));

        // 4. 过滤出根节点（比如 parentId = 0 的节点）返回给前端，只返回5个顶级菜单
        return allPermissions.stream()
                .filter(p -> p.getParentId() == 0)
                .collect(Collectors.toList());
    }
}
