package org.example.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.cms.entity.User;

import java.util.List;

@Mapper
public interface UserMapper  extends BaseMapper<User> {
    List<User> getAllUser();
}
