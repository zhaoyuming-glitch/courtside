package com.jr.service;

import com.jr.entity.Post;
import com.jr.util.PageHelper;

import java.util.List;

public interface PostService {

   PageHelper<Post>showPost(String sort, int indexPage, String keyWord);
   List<Post> getIndexPagePost(String sort,String source,int number);
   Post getPostDetail(int postId);


}
