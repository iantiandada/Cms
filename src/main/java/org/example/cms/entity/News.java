package org.example.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class News {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private Integer category;
    private String content;
    private String supplier;
    private String reviewer;
    private String status;
    private Date createTime;
    private Date updateTime;
    private Date publishTime;
}