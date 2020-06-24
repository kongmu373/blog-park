package com.kongmu373.park.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kongmu373.park.entity.Blog;
import com.kongmu373.park.entity.BlogVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {
    List<BlogVo> selectListByUserId(@Param("user_id") Integer userId,
                                    @Param("offset") Integer offset,
                                    @Param("limit") Integer limit);

    default Integer selectCountByUserId(Integer userId) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        Blog blog = new Blog().setUserId(userId);
        wrapper.setEntity(blog);
        return selectCount(wrapper);
    }

    BlogVo selectByIdWithUser(@Param("blog_id") Integer blogId);
}
