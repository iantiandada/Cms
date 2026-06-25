package org.example.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.cms.entity.News;

import java.util.List;

@Mapper
public interface NewsMapper extends BaseMapper<News> {
    List<News> newsList(int start, int rows);
    int newsCount();
    // 在 NewsMapper.java 中添加以下方法
    List<News> getNewsByCategory(@Param("category") int category,
                                 @Param("start") int start,
                                 @Param("rows") int rows);
    int countByCategory(@Param("category") int category);
}
