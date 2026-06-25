package org.example.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.cms.entity.Permission;
import org.example.cms.entity.Role;

import java.util.List;


@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    List<Permission> getAllPermission() ;
    List<Integer> getPermissionCurrentRole(Integer roleId);
    List<Integer> getPermissionIdsByUserId(Integer userId);
}




