package com.example.dao;

import com.example.entity.CommentInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CommentInfoDao extends Mapper<CommentInfo> {

    List<CommentInfo> findByContent(@Param("name") String name);

    @Select("select a.*,c.name as userName from comment_info as a left join user_info as c on a.userId = c.id where goodsId = #{goodsId} and userId = #{userId}")
    List<CommentInfo> findByGoodsIdAndUserId(@Param("goodsId") Long goodsId, @Param("userId") Long userId);

    @Select("select a.*,c.name as userName from comment_info as a left join user_info as c on a.userId = c.id where goodsId = #{goodsId}")
    List<CommentInfo> findByGoodsId(@Param("goodsId") Long goodsId);

    @Select("select count(id) from comment_info")
    Integer count();
}