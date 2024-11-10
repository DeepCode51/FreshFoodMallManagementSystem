package com.example.service;

import com.example.dao.MessageInfoDao;
import com.example.entity.MessageInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class MessageInfoService {

    @Resource
    private MessageInfoDao messageInfoDao;

    public MessageInfo add(MessageInfo messageInfo) {
        messageInfoDao.insertSelective(messageInfo);
        return messageInfo;
    }

    public void delete(Long id) {
        messageInfoDao.deleteByPrimaryKey(id);
    }

    public void update(MessageInfo messageInfo) {
        messageInfoDao.updateByPrimaryKeySelective(messageInfo);
    }

    public MessageInfo findById(Long id) {
        return messageInfoDao.selectByPrimaryKey(id);
    }

    public List<MessageInfo> findAll() {
        return messageInfoDao.findByName("all");
    }

    public PageInfo<MessageInfo> findPage(String name, Integer pageNum, Integer pageSize, HttpServletRequest request) {
        PageHelper.startPage(pageNum, pageSize);
        List<MessageInfo> all = findAllPage(request, name);
        return PageInfo.of(all);
    }

    public List<MessageInfo> findAllPage(HttpServletRequest request, String name) {
		return messageInfoDao.findByName(name);
    }

}
