package com.example.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.example.dao.CommentInfoDao;
import com.example.dao.GoodsInfoDao;
import com.example.entity.CommentInfo;
import com.example.entity.GoodsInfo;
import com.example.entity.TypeInfo;
import com.example.entity.UserInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class GoodsInfoService {

    @Resource
    private GoodsInfoDao goodsInfoDao;
    @Resource
    private TypeInfoService typeInfoService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private NxSystemFileInfoService nxSystemFileInfoService;
    @Resource
    private CommentInfoDao commentInfoDao;

    public GoodsInfo add(GoodsInfo goodsInfo) {
        List<Long> fileList = goodsInfo.getFileList();
        if (!CollectionUtil.isEmpty(fileList)) {
            goodsInfo.setFileIds(fileList.toString());
        }
        goodsInfoDao.insertSelective(goodsInfo);
        return goodsInfo;
    }

    public void delete(Long id) {
        goodsInfoDao.deleteByPrimaryKey(id);
    }

    public void update(GoodsInfo goodsInfo) {
        List<Long> fileList = goodsInfo.getFileList();
        if (!CollectionUtil.isEmpty(fileList)) {
            goodsInfo.setFileIds(fileList.toString());
        }
        if (goodsInfo.getHot() != null) {
            GoodsInfo goods = findById(goodsInfo.getId());
            goodsInfo.setHot(goods.getHot() + goodsInfo.getHot());
        }
        goodsInfoDao.updateByPrimaryKeySelective(goodsInfo);
    }

    public GoodsInfo findById(Long id) {
        GoodsInfo goodsInfo = goodsInfoDao.selectByPrimaryKey(id);
        getRelInfo(goodsInfo);
        return goodsInfo;
    }

    public List<GoodsInfo> findAll() {
        return goodsInfoDao.selectAll();
    }

    public PageInfo<GoodsInfo> findPage(Integer pageNum, Integer pageSize, String name, HttpServletRequest request) {
        PageHelper.startPage(pageNum, pageSize);
        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        List<GoodsInfo> all;
        if (user.getLevel() == 1) {
            all = goodsInfoDao.findByNameAndUserId(name, null);
        } else {
            all = goodsInfoDao.findByNameAndUserId(name, user.getId());
        }
        for (GoodsInfo goodsInfo : all) {
            getRelInfo(goodsInfo);
        }
        return PageInfo.of(all);
    }

    public PageInfo<GoodsInfo> findAllGoods(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsInfo> goodsInfos = goodsInfoDao.selectAll();
        return PageInfo.of(goodsInfos);
    }

    public List<GoodsInfo> findByType(Integer typeId) {
        return goodsInfoDao.findByType(typeId);
    }

    public PageInfo<GoodsInfo> findRecommendGoods(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsInfo> list = goodsInfoDao.findRecommendGoods();
        return PageInfo.of(list);
    }

    public PageInfo<GoodsInfo> findHotSalesGoods(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsInfo> list = goodsInfoDao.findHotSalesGoods();
        return PageInfo.of(list);
    }

    private void getRelInfo(GoodsInfo goodsInfo) {
        if (goodsInfo == null) {
            return;
        }
        TypeInfo typeInfo = typeInfoService.findById(goodsInfo.getTypeId());
        if (typeInfo != null) {
            goodsInfo.setTypeName(typeInfo.getName());
        }
        UserInfo userInfo = userInfoService.findById(goodsInfo.getUserId());
        if (userInfo != null) {
            goodsInfo.setUserName(userInfo.getName());
        }

//            NxSystemFileInfo fileInfo = nxSystemFileInfoService.findById(Long.parseLong(goodsInfo.getFileId()));
//            if (fileInfo != null) {
//                goodsInfo.setFileName(fileInfo.getOriginName());
//        }

    }

    /**
     * 查询用户买过的所有商品
     *
     * @param userId
     * @return
     */
    public List<GoodsInfo> getOrderGoods(Long userId) {
        List<GoodsInfo> list = goodsInfoDao.getOrderGoods(userId);
        for (GoodsInfo goodsInfo : list) {
            List<CommentInfo> commentInfoList = commentInfoDao.findByGoodsIdAndUserId(goodsInfo.getId(), userId);
            if (CollUtil.isEmpty(commentInfoList)) {
                goodsInfo.setCommentStatus("未评价");
            } else {
                goodsInfo.setCommentStatus("已评价");
            }
        }
        return list;
    }

    public List<GoodsInfo> searchGoods(String text) {
        return goodsInfoDao.findByText(text);
    }
}
