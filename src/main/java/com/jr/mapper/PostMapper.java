package com.jr.mapper;

import com.jr.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PostMapper {
    int insert(Post post);
    Post selectById(Integer postId);
    int update(Post post);
    int deleteById(Integer postId);
    List<Post> selectOfficialPosts(@Param("limit") int limit);
    List<Post> selectHottestPosts(@Param("limit") int limit);
    List<Post> searchPosts(@Param("keyword") String keyword,
                           @Param("sortType") Integer sortType,
                           @Param("offset") int offset,
                           @Param("limit") int limit);
    int countPosts(@Param("keyword") String keyword);
}