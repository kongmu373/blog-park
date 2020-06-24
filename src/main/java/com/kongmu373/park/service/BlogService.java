package com.kongmu373.park.service;

import com.kongmu373.park.common.BlogPageResult;
import com.kongmu373.park.common.CommonResult;
import com.kongmu373.park.entity.Blog;
import com.kongmu373.park.entity.BlogVo;
import com.kongmu373.park.entity.User;
import com.kongmu373.park.mapper.BlogMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static com.kongmu373.park.utils.ValidUtils.validBlog;
import static com.kongmu373.park.utils.ValidUtils.validParamAndReturnResult;

@Service
public class BlogService {

    private BlogMapper blogMapper;

    @Inject
    public BlogService(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;
    }

    public CommonResult<List<BlogVo>> selectListByUserId(Integer page, Integer pageSize, Integer userId) {
        try {
            List<BlogVo> blogVos = blogMapper.selectListByUserId(userId, (page - 1) * pageSize, pageSize);
            Integer count = blogMapper.selectCountByUserId(userId);
            int totalPage = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            return BlogPageResult.success("获取成功", blogVos, count, page, totalPage);
        } catch (RuntimeException e) {
            return CommonResult.fail(e.getMessage());
        }

    }


    public CommonResult<BlogVo> getBlogById(int blogId) {
        try {
            return CommonResult.success("获取成功", blogMapper.selectByIdWithUser(blogId));
        } catch (RuntimeException e) {
            return CommonResult.fail(e.getMessage());
        }
    }


    public CommonResult<BlogVo> update(int blogId, Map<String, String> params, User user) {
        try {
            Blog paramBlog = setBlog(blogId, params, user);
            return Optional.ofNullable(blogMapper.selectByIdWithUser(blogId))
                    .map(blogVo -> returnCommonResultForMethod(blogVo, user.getId(), updateByIdAndReturnSuccess(paramBlog)))
                    .orElse(CommonResult.fail("博客不存在"));
        } catch (RuntimeException e) {
            return CommonResult.fail(e.getMessage());
        }
    }

    private Supplier<CommonResult<BlogVo>> updateByIdAndReturnSuccess(Blog paramBlog) {
        return () -> {
            blogMapper.updateById(paramBlog);
            return CommonResult.success("修改成功", blogMapper.selectByIdWithUser(paramBlog.getId()));
        };
    }


    public CommonResult<BlogVo> createBlog(User user, Map<String, String> params) {
        try {
            Blog blog = setBlog(null, params, user);
            blogMapper.insert(blog);
            BlogVo blogVo = blogMapper.selectByIdWithUser(blog.getId());
            return CommonResult.success("创建成功", blogVo);
        } catch (RuntimeException e) {
            return CommonResult.fail(e.getMessage());
        }
    }

    public CommonResult<BlogVo> deleteById(int blogId, int userId) {
        try {
            return Optional.ofNullable(blogMapper.selectByIdWithUser(blogId))
                    .map(blogVo -> returnCommonResultForMethod(blogVo, userId, deletedByIdAndReturnSuccess(blogId)))
                    .orElse(CommonResult.fail("博客不存在"));

        } catch (RuntimeException e) {
            return CommonResult.fail(e.getMessage());
        }
    }

    private Supplier<CommonResult<BlogVo>> deletedByIdAndReturnSuccess(int blogId) {
        return () -> {
            blogMapper.deleteById(blogId);
            return CommonResult.success("删除成功", null);
        };
    }

    private CommonResult<BlogVo> returnCommonResultForMethod(BlogVo blogVo, Integer userId, Supplier<CommonResult<BlogVo>> supplier) {
        if (blogVo.getUser().getId() != userId) {
            return CommonResult.fail("无法修改或删除别人的博客");
        }
        return supplier.get();
    }

    private Blog setBlog(Integer blogId, Map<String, String> params, User user) {
        String title = params.get("title");
        String content = params.get("content");
        String description = params.get("description");
        validParamAndReturnResult(validBlog(title, content));
        if (StringUtils.isBlank(description)) {
            description = content.substring(0, Math.min(content.length(), 10)) + "...";
        }
        return new Blog().setId(blogId)
                .setUserId(user.getId())
                .setTitle(title)
                .setContent(content)
                .setDescription(description);
    }
}
