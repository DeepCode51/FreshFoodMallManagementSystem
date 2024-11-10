package com.example.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.example.common.Result;
import com.example.entity.MessageInfo;
import com.example.service.MessageInfoService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/messageInfo")
public class MessageInfoController {
    @Resource
    private MessageInfoService messageInfoService;

    @PostMapping
    public Result<MessageInfo> add(@RequestBody MessageInfo messageInfo) {
        messageInfo.setTime(DateUtil.formatDateTime(new Date()));
        messageInfoService.add(messageInfo);
        return Result.success(messageInfo);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        messageInfoService.delete(id);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody MessageInfo messageInfo) {
        messageInfoService.update(messageInfo);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<MessageInfo> detail(@PathVariable Long id) {
        MessageInfo messageInfo = messageInfoService.findById(id);
        return Result.success(messageInfo);
    }

    @GetMapping
    public Result<List<MessageInfo>> all() {
        return Result.success(messageInfoService.findAll());
    }

    @GetMapping("/page/{name}")
    public Result<PageInfo<MessageInfo>> page(@PathVariable String name,
                                              @RequestParam(defaultValue = "1") Integer pageNum,
                                              @RequestParam(defaultValue = "5") Integer pageSize,
                                              HttpServletRequest request) {
        return Result.success(messageInfoService.findPage(name, pageNum, pageSize, request));
    }

    /**
     * 批量通过excel添加信息
     * @param file excel文件
     * @throws IOException
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {

        List<MessageInfo> infoList = ExcelUtil.getReader(file.getInputStream()).readAll(MessageInfo.class);
        if (!CollectionUtil.isEmpty(infoList)) {
            // 处理一下空数据
            List<MessageInfo> resultList = infoList.stream().filter(x -> ObjectUtil.isNotEmpty(x.getName())).collect(Collectors.toList());
            for (MessageInfo info : resultList) {
                messageInfoService.add(info);
            }
        }
        return Result.success();
    }
}
