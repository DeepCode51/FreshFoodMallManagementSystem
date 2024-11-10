package com.example.controller;

import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.entity.GoodsInfo;
import com.example.entity.OrderInfo;
import com.example.exception.CustomException;
import com.example.service.OrderInfoService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/orderInfo")
public class OrderInfoController {
    @Resource
    private OrderInfoService orderInfoService;

    /**
     * 根据id查询
     */
    @GetMapping("/order/{id}")
    public Result<OrderInfo> findById(@PathVariable Long id) {
        return Result.success(orderInfoService.findById(id));
    }

    /**
     * 根据订单id查询
     */
    @GetMapping("/{orderId}")
    public Result<OrderInfo> findByOrderId(@PathVariable Long orderId) {
        return Result.success(orderInfoService.findByOrderId(orderId));
    }

    /**
     * 查询所有信息（不分页）
     */
    @GetMapping
    public Result<List<OrderInfo>> findAll(@RequestParam Long userId) {
        return Result.success(orderInfoService.findAll(userId));
    }

    /**
     * 查询所有信息（分页）
     */
    @GetMapping("/page")
    public Result<PageInfo<OrderInfo>> findPage(@RequestParam(required = false) Long userId,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                HttpServletRequest request) {
        return Result.success(orderInfoService.findPages(userId, pageNum, pageSize, request));
    }

    /**
     * 查询所有信息（分页）
     */
    @GetMapping("/page/front")
    public Result<PageInfo<OrderInfo>> findFrontPage(@RequestParam(required = false) Long userId,
                                                     @RequestParam(required = false) String status,
                                                     @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return Result.success(orderInfoService.findFrontPages(userId, status, pageNum, pageSize));
    }

    /**
     * 下单
     *
     * @return
     */
    @PostMapping
    public Result<OrderInfo> add(@RequestBody OrderInfo orderInfo) {
        Long userId = orderInfo.getUserId();
        List<GoodsInfo> goodsList = orderInfo.getGoodsList();
        if (userId == null || goodsList == null || goodsList.size() == 0) {
            throw new CustomException(ResultCode.PARAM_ERROR);
        }
        orderInfo.setStatus("待付款");
        return Result.success(orderInfoService.add(orderInfo));
    }

    /**
     * 小程序版，分别下单
     */
    @PostMapping("/dis")
    public Result addDis(@RequestBody OrderInfo orderInfo) {
        Long userId = orderInfo.getUserId();
        List<GoodsInfo> goodsList = orderInfo.getGoodsList();
        if (userId == null || goodsList == null || goodsList.size() == 0) {
            throw new CustomException(ResultCode.PARAM_ERROR);
        }
        orderInfoService.add(userId, goodsList);
        return Result.success();
    }

    /**
     * 修改订单状态
     */
    @PostMapping("/status/{id}/{status}")
    public Result status(@PathVariable Long id, @PathVariable String status) {
        orderInfoService.changeStatus(id, status);
        return Result.success();
    }


    /**
     * 删除历史订单
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        orderInfoService.delete(id);
        return Result.success();
    }

    @DeleteMapping()
    public Result delete(@RequestParam Long goodsId, @RequestParam Long id) {
        orderInfoService.deleteGoods(goodsId, id);
        return Result.success();
    }
}
