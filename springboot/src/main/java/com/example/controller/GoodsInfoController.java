package com.example.controller;

import com.example.common.Result;
import com.example.entity.GoodsInfo;
import com.example.entity.UserInfo;
import com.example.service.GoodsInfoService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class GoodsInfoController {
    @Resource
    private GoodsInfoService goodsInfoService;

    @PostMapping("/goodsInfo")
    public Result<GoodsInfo> add(@RequestBody GoodsInfo goodsInfo, HttpServletRequest request) {
        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        goodsInfo.setUserId(user.getId());
        goodsInfoService.add(goodsInfo);
        return Result.success(goodsInfo);
    }

    @DeleteMapping("/goodsInfo/{id}")
    public Result delete(@PathVariable Long id) {
        goodsInfoService.delete(id);
        return Result.success();
    }

    @PutMapping("/goodsInfo")
    public Result update(@RequestBody GoodsInfo goodsInfo) {
        goodsInfoService.update(goodsInfo);
        return Result.success();
    }

    @GetMapping("/goodsInfo/{id}")
    public Result<GoodsInfo> detail(@PathVariable Long id) {
        GoodsInfo goodsInfo = goodsInfoService.findById(id);
        return Result.success(goodsInfo);
    }

    @GetMapping("/goodsInfo")
    public Result<List<GoodsInfo>> all() {
        return Result.success(goodsInfoService.findAll());
    }

    @GetMapping("/goodsInfo/page/{name}")
    public Result<PageInfo<GoodsInfo>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                            @PathVariable String name,
                                            HttpServletRequest request) {
        return Result.success(goodsInfoService.findPage(pageNum, pageSize, name, request));
    }

    @GetMapping("/goodsInfo/findByType/{typeId}")
    public Result<List<GoodsInfo>> findByType(@PathVariable Integer typeId) {
        return Result.success(goodsInfoService.findByType(typeId));
    }

    /**
     * 获取推荐商品
     */
    @GetMapping("/goodsInfoRecommend/page/all")
    public Result<PageInfo<GoodsInfo>> recommendGoods(@RequestParam(defaultValue = "1") Integer pageNum,
                                                      @RequestParam(defaultValue = "100") Integer pageSize) {
        return Result.success(goodsInfoService.findRecommendGoods(pageNum, pageSize));
    }

    /**
     * 获取所有商品带分页
     */
    @GetMapping("/goodsInfoGoods/page/all")
    public Result<PageInfo<GoodsInfo>> pageGoods(@RequestParam(defaultValue = "1") Integer pageNum,
                                                 @RequestParam(defaultValue = "100") Integer pageSize) {
        return Result.success(goodsInfoService.findAllGoods(pageNum, pageSize));
    }

    /**
     * 获取轮播图商品
     */
    @GetMapping("/goodsInfoCarousel/page/all")
    public Result<PageInfo<GoodsInfo>> pageGoodsCarousel(@RequestParam(defaultValue = "1") Integer pageNum,
                                                         @RequestParam(defaultValue = "3") Integer pageSize) {
        return Result.success(goodsInfoService.findAllGoods(pageNum, pageSize));
    }

    /**
     * 获取热卖商品
     */
    @GetMapping("/goodsInfoHot/page/all")
    public Result<PageInfo<GoodsInfo>> sales(@RequestParam(defaultValue = "1") Integer pageNum,
                                             @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(goodsInfoService.findHotSalesGoods(pageNum, pageSize));
    }

    /**
     * 查询用户买到过的所有商品
     * @return
     */
    @GetMapping("/goodsInfo/comment/{userId}")
    public Result<List<GoodsInfo>> orderGoods(@PathVariable Long userId) {
        return Result.success(goodsInfoService.getOrderGoods(userId));
    }

    @GetMapping("/goodsInfo/searchGoods")
    public Result<List<GoodsInfo>> searchGoods(@RequestParam String text) {
        return Result.success(goodsInfoService.searchGoods(text));
    }
}
