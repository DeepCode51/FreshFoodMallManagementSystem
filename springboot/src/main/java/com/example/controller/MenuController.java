package com.example.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.example.entity.UserInfo;
import com.example.exception.CustomException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MenuController {


    @GetMapping(value = "/getMenu", produces = "application/json;charset=UTF-8")
    public String getMenu(HttpServletRequest request) {
		UserInfo account = (UserInfo) request.getSession().getAttribute("user");
        Integer level;
        if (account == null) {
            throw new CustomException("1001", "session已失效，请重新登录");
        }

        level = account.getLevel();

        JSONObject obj = new JSONObject();
        obj.putOpt("code", 0);
        obj.putOpt("msg", "");
        JSONArray dataArray = new JSONArray();

        dataArray.add(getJsonObject("/", "系统首页", "layui-icon-home", "/"));

        JSONObject tableObj = new JSONObject();
        tableObj.putOpt("title", "信息管理");
        tableObj.putOpt("icon", "layui-icon-table");
        JSONArray array = new JSONArray();
        array.add(getJsonObject("userInfo", "用户信息", "layui-icon-user", "userInfo"));
        array.add(getJsonObject("advertiserInfo", "公告信息", "layui-icon-table", "advertiserInfo"));
        array.add(getJsonObject("typeInfo", "餐饮类别", "layui-icon-table", "typeInfo"));
        array.add(getJsonObject("goodsInfo", "菜肴详情", "layui-icon-table", "goodsInfo"));
        array.add(getJsonObject("orderInfo", "订单信息", "layui-icon-table", "orderInfo"));
        array.add(getJsonObject("cartInfo", "想吃信息", "layui-icon-table", "cartInfo"));
        array.add(getJsonObject("messageInfo", "留言信息", "layui-icon-table", "messageInfo"));
        array.add(getJsonObject("commentInfo", "评论信息", "layui-icon-table", "commentInfo"));
        tableObj.putOpt("list", array);

        dataArray.add(tableObj);
        dataArray.add(getJsonObject("accountUserInfo", "个人信息", "layui-icon-username", "accountUserInfo"));
        dataArray.add(getJsonObject("updatePassword", "修改密码", "layui-icon-password", "updatePassword"));
        dataArray.add(getJsonObject("login", "退出登录", "layui-icon-logout", "login"));

        obj.putOpt("data", dataArray);
        return obj.toString();
    }

    private JSONObject getJsonObject(String name, String title, String icon, String jump) {
        JSONObject object = new JSONObject();
        object.putOpt("name", name);
        object.putOpt("title", title);
        object.putOpt("icon", icon);
        object.putOpt("jump", jump);
        return object;
    }

}
