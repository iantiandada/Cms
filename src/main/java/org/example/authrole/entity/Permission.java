package org.example.authrole.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("permission")
public class Permission {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    @TableField("type")
    private MenuType menuType;
    private String code;
    private String path;
    private Integer parentId;
    private Permission parent;
    @TableField(exist = false)
    private List<Permission> children;
    private Integer sort;
    private LocalDateTime createTime;
}
