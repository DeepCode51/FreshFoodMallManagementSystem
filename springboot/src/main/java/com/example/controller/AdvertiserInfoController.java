package com.example.controller;

import com.example.common.Result;
import com.example.entity.AdvertiserInfo;
import com.example.service.AdvertiserInfoService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/advertiserInfo")
public class AdvertiserInfoController {
    @Resource
    private AdvertiserInfoService advertiserInfoService;

    @PostMapping
    public Result<AdvertiserInfo> add(@RequestBody AdvertiserInfo advertiserInfo) {
        advertiserInfoService.add(advertiserInfo);
        return Result.success(advertiserInfo);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        advertiserInfoService.delete(id);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody AdvertiserInfo advertiserInfo) {
        advertiserInfoService.update(advertiserInfo);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<AdvertiserInfo> detail(@PathVariable Long id) {
        AdvertiserInfo advertiserInfo = advertiserInfoService.findById(id);
        return Result.success(advertiserInfo);
    }

    @GetMapping
    public Result<List<AdvertiserInfo>> all() {
        return Result.success(advertiserInfoService.findAll());
    }

    @GetMapping("/page/{name}")
    public Result<PageInfo<AdvertiserInfo>> page(@PathVariable String name,
                                                   @RequestParam(defaultValue = "1") Integer pageNum,
                                                   @RequestParam(defaultValue = "5") Integer pageSize,
                                                   HttpServletRequest request) {
        return Result.success(advertiserInfoService.findPage(name, pageNum, pageSize, request));
    }
}
