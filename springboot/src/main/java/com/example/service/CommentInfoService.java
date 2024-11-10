package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.dao.CommentInfoDao;
import com.example.entity.CommentInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class CommentInfoService {

    @Resource
    private CommentInfoDao commentInfoDao;

    public CommentInfo add(CommentInfo commentInfo) {
        commentInfo.setCreateTime(DateUtil.formatDateTime(new Date()));
        String content = commentInfo.getContent();
        if (content.length() > 255) {
            commentInfo.setContent(content.substring(0, 250));
        }
        commentInfoDao.insertSelective(commentInfo);
        return commentInfo;
    }

    public void delete(Long id) {
        commentInfoDao.deleteByPrimaryKey(id);
    }

    public void update(CommentInfo commentInfo) {
        String content = commentInfo.getContent();
        if (content.length() > 255) {
            commentInfo.setContent(content.substring(0, 250));
        }
        commentInfoDao.updateByPrimaryKeySelective(commentInfo);
    }

    public CommentInfo findById(Long id) {
        return commentInfoDao.selectByPrimaryKey(id);
    }

    public List<CommentInfo> findAll() {
        return commentInfoDao.selectAll();
    }

    public List<CommentInfo> findAll(Long goodsId) {
        return commentInfoDao.findByGoodsId(goodsId);
    }

    public PageInfo<CommentInfo> findPage(Integer pageNum, Integer pageSize, String name) {
        PageHelper.startPage(pageNum, pageSize);
        List<CommentInfo> all = commentInfoDao.findByContent(name);
        return PageInfo.of(all);
    }

}
