package org.example.authrole.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.authrole.dto.AuthRoleDTO;
import org.example.authrole.entity.Permission;
import org.example.authrole.entity.Role;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    void authRole(AuthRoleDTO authRoleDTO);

    void deleteRolePermission(Integer roleId);

    List<Permission> getAllPermission();
}
