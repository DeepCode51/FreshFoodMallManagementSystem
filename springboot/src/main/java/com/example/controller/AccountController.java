package com.example.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.entity.UserInfo;
import com.example.entity.Account;
import com.example.exception.CustomException;
import com.example.service.UserInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AccountController {

    @Resource
    private UserInfoService userInfoService;

    @GetMapping("/logout")
    public Result logout(HttpServletRequest request) {
        request.getSession().setAttribute("user", null);
        return Result.success();
    }

    @GetMapping("/auth")
    public Result getAuth(HttpServletRequest request) {
        Object user = request.getSession().getAttribute("user");
        if(user == null) {
            return Result.error("401", "未登录");
        }
        return Result.success((UserInfo)user);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result<UserInfo> register(@RequestBody UserInfo userInfo, HttpServletRequest request) {
        if (StrUtil.isBlank(userInfo.getName()) || StrUtil.isBlank(userInfo.getPassword())) {
            throw new CustomException(ResultCode.PARAM_ERROR);
        }
        UserInfo register = userInfoService.add(userInfo);
        HttpSession session = request.getSession();
        session.setAttribute("user", register);
        session.setMaxInactiveInterval(120 * 60);
        return Result.success(register);
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<UserInfo> login(@RequestBody UserInfo userInfo, HttpServletRequest request) {
        if (StrUtil.isBlank(userInfo.getName()) || StrUtil.isBlank(userInfo.getPassword())) {
            throw new CustomException(ResultCode.USER_ACCOUNT_ERROR);
        }
        UserInfo login = userInfoService.login(userInfo.getName(), userInfo.getPassword());
        HttpSession session = request.getSession();
        session.setAttribute("user", login);
        session.setMaxInactiveInterval(120 * 60);
        return Result.success(login);
    }

    /**
     * 重置密码为123456
     */
    @PutMapping("/resetPassword")
    public Result<UserInfo> resetPassword(@RequestParam String username) {
        return Result.success(userInfoService.resetPassword(username));
    }

    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody UserInfo info, HttpServletRequest request) {
        UserInfo account = (UserInfo) request.getSession().getAttribute("user");
        if (account == null) {
            return Result.error(ResultCode.USER_NOT_EXIST_ERROR.code, ResultCode.USER_NOT_EXIST_ERROR.msg);
        }
        String oldPassword = SecureUtil.md5(info.getPassword());
        if (!oldPassword.equals(account.getPassword())) {
            return Result.error(ResultCode.PARAM_PASSWORD_ERROR.code, ResultCode.PARAM_PASSWORD_ERROR.msg);
        }
        account.setPassword(SecureUtil.md5(info.getNewPassword()));
        userInfoService.update(account);

        // 清空session，让用户重新登录
        request.getSession().setAttribute("user", null);
        return Result.success();
    }

    @GetMapping("/mini/userInfo/{id}/{level}")
    public Result<Account> miniLogin(@PathVariable Long id, @PathVariable Integer level) {
        Account account = userInfoService.findByIdAndLevel(id, level);
        return Result.success(account);
    }

    /**
     * 修改密码
     */
    @PutMapping("/changePassword")
    public Result<Boolean> changePassword(@RequestParam Long id,
                                          @RequestParam String newPassword) {
        return Result.success(userInfoService.changePassword(id, newPassword));
    }

    @GetMapping("/getSession")
    public Result<Map<String, String>> getSession(HttpServletRequest request) {
        UserInfo account = (UserInfo) request.getSession().getAttribute("user");
        if (account == null) {
            return Result.success(new HashMap<>(1));
        }
        Map<String, String> map = new HashMap<>(1);
        map.put("username", account.getName());
        return Result.success(map);
    }
}
