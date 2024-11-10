package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.crypto.SecureUtil;
import com.example.common.ResultCode;
import com.example.dao.UserInfoDao;
import com.example.exception.CustomException;
import org.springframework.stereotype.Service;
import com.example.entity.UserInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class UserInfoService {

    @Resource
    private UserInfoDao userInfoDao;

    /**
     * 新增用户
     */
    public UserInfo add(UserInfo userInfo) {
        // 唯一校验
        int count = userInfoDao.checkRepeat("name", userInfo.getName(), null);
        if (count > 0) {
            throw new CustomException(ResultCode.USER_EXIST_ERROR);
        }
        if (userInfo.getPassword() == null) {
            // 默认密码123456
            userInfo.setPassword(SecureUtil.md5("123456"));
        } else {
            userInfo.setPassword(SecureUtil.md5(userInfo.getPassword()));
        }
        // 注册用户默认是买家
        userInfo.setLevel(3);
        userInfoDao.insertSelective(userInfo);
        return userInfo;
    }

    public void delete(Long id) {
        userInfoDao.deleteByPrimaryKey(id);
    }

    public void update(UserInfo userInfo) {
        userInfoDao.updateByPrimaryKeySelective(userInfo);
    }

    public UserInfo findById(Long id) {
        return userInfoDao.selectByPrimaryKey(id);
    }

    public UserInfo findByIdAndLevel(Long id, Integer level) {
        Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("id", id)
                .andEqualTo("level", level);
        List<UserInfo> list = userInfoDao.selectByExample(example);
        return list.get(0);
    }

    public List<UserInfo> findAll() {
        return userInfoDao.selectAll();
    }

    public PageInfo<UserInfo> findPage(Integer pageNum, Integer pageSize, String name) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserInfo> all = userInfoDao.findByName(name);
        return PageInfo.of(all);
    }

    /**
     * 登录
     */
    public UserInfo login(String name, String password) {
        List<UserInfo> list = userInfoDao.findByName(name);
        if (CollectionUtil.isEmpty(list)) {
            throw new CustomException(ResultCode.USER_NOT_EXIST_ERROR);
        }
        if (!SecureUtil.md5(password).equalsIgnoreCase(list.get(0).getPassword())) {
            throw new CustomException(ResultCode.USER_ACCOUNT_ERROR);
        }

        return list.get(0);
    }

    /**
     * 重置密码（忘记密码）
     */
    public UserInfo resetPassword(String name) {
        List<UserInfo> list = userInfoDao.findByName(name);
        if (CollectionUtil.isEmpty(list)) {
            throw new CustomException(ResultCode.USER_NOT_EXIST_ERROR);
        }
        list.get(0).setPassword(SecureUtil.md5("123456"));
        update(list.get(0));
        return list.get(0);
    }

    /**
     * 修改密码
     */
    public boolean changePassword(Long id, String newPassword) {
        UserInfo userInfo = findById(id);
        if(userInfo == null) {
            throw new CustomException(ResultCode.USER_NOT_EXIST_ERROR);
        }
        userInfo.setPassword(SecureUtil.md5(newPassword));
        update(userInfo);
        return true;
    }

}
