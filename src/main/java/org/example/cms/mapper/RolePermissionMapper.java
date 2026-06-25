package org.example.cms.mapper;

import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface RolePermissionMapper {

    // 先清空该角色的所有权限
    @Delete("DELETE FROM role_permission WHERE role_id = #{roleId}")
    void deleteByRoleId(@Param("roleId") Integer roleId);

    // 批量插入新权限
    @Insert("<script>" +
            "INSERT INTO role_permission (role_id, permission_id) VALUES " +
            "<foreach collection='permissionIds' item='pid' separator=','>" +
            "(#{roleId}, #{pid})" +
            "</foreach>" +
            "</script>")
    void insertBatch(@Param("roleId") Integer roleId,
                     @Param("permissionIds") List<Integer> permissionIds);
}