package com.example.service;

import com.example.dao.AdvertiserInfoDao;
import com.example.entity.AdvertiserInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AdvertiserInfoService {

    @Resource
    private AdvertiserInfoDao advertiserInfoDao;

    public AdvertiserInfo add(AdvertiserInfo advertiserInfo) {
        advertiserInfo.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        advertiserInfoDao.insertSelective(advertiserInfo);
        return advertiserInfo;
    }

    public void delete(Long id) {
        advertiserInfoDao.deleteByPrimaryKey(id);
    }

    public void update(AdvertiserInfo advertiserInfo) {
        advertiserInfo.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        advertiserInfoDao.updateByPrimaryKeySelective(advertiserInfo);
    }

    public AdvertiserInfo findById(Long id) {
        return advertiserInfoDao.selectByPrimaryKey(id);
    }

    public List<AdvertiserInfo> findAll() {
        return advertiserInfoDao.findByName("all");
    }

    public PageInfo<AdvertiserInfo> findPage(String name, Integer pageNum, Integer pageSize, HttpServletRequest request) {
        PageHelper.startPage(pageNum, pageSize);
        List<AdvertiserInfo> all = findAllPage(request, name);
        return PageInfo.of(all);
    }

    public List<AdvertiserInfo> findAllPage(HttpServletRequest request, String name) {
		return advertiserInfoDao.findByName(name);
    }

}
