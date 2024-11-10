package com.example.dao;

import com.example.entity.CartInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CartInfoDao extends Mapper<CartInfo> {
    List<CartInfo> findCartByUserId(Long userId);

    List<CartInfo> findAll();

    @Delete("delete from cart_info where userId = #{userId}")
    int deleteByUserId(@Param("userId") Long userId);

    @Delete("delete from cart_info where userId = #{userId} and goodsId = #{goodsId}")
    int deleteGoods(@Param("userId") Long userId, @Param("goodsId") Long goodsId);
}