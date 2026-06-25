package org.example.cms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.cms.common.Result;
import org.example.cms.dto.LoginDTO;
import org.example.cms.dto.UserDTO;
import org.example.cms.entity.User;
import org.example.cms.entity.UserRole;
import org.example.cms.mapper.RoleMapper;
import org.example.cms.mapper.UserMapper;
import org.example.cms.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;   // 注入中间表Mapper

    @GetMapping("/getAllUser")
    public List<User> getAllUser() {
        return userMapper.getAllUser();
    }

    @PostMapping("/addUser")
    public String addUser(@RequestBody UserDTO dto) {
        // 1. 插入用户
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        userMapper.insert(user);

        // 2. 如果选择了角色，插入中间表
        if (dto.getRoleId() != null) {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(dto.getRoleId());
            userRoleMapper.insert(userRole);
        }
        return "添加成功";
    }

    @PutMapping("/updateUser/{id}")
    public String updateUser(@PathVariable Integer id, @RequestBody UserDTO dto) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return "用户不存在";
        }
        // 更新用户基础信息
        user.setUsername(dto.getUsername());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(dto.getPassword());
        }
        userMapper.updateById(user);

        // 更新角色：先删除旧关系，再插入新关系
        // 删除该用户所有旧角色关联
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, id));
        // 插入新角色
        if (dto.getRoleId() != null) {
            UserRole userRole = new UserRole();
            userRole.setUserId(id);
            userRole.setRoleId(dto.getRoleId());
            userRoleMapper.insert(userRole);
        }
        return "修改成功";
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Integer id) {
        // 先删除中间表关联，再删用户
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, id));
        userMapper.deleteById(id);
        return "删除成功";
    }

    //登陆
    @Autowired
    private RoleMapper roleMapper;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO dto) {
        // 1. 根据用户名查询用户
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername())
        );

        if (user == null) {
            return Result.error("用户不存在");
        }
        if (!user.getPassword().equals(dto.getPassword())) {
            return Result.error("密码错误");
        }

        // 2. 查询该用户所有权限ID
        List<Integer> permissionIds = roleMapper.getPermissionIdsByUserId(user.getId());

        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("permissionIds", permissionIds);
        return Result.success(data);
    }
}