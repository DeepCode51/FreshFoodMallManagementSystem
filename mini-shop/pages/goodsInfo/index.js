import {request} from '../../request/index.js'
import {config} from '../../request/config'

Page({

  /**
   * 页面的初始数据
   */
  data: {
    obj: {},
    goodsId: 0,
    swiperList: [],
    commentList: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    const id = options.id;
    this.setData({
      goodsId: id
    })
    // 获取数据
    this.getDetail(id);
    this.getComment(id);
  },
  getDetail(id) {
    request({url: '/goodsInfo/' + id}).then(res => {
      if(res.code === '0') {
        let obj = res.data;
        let swiperList = [];
        if(obj.fileIds) {
          let list = JSON.parse(obj.fileIds);
          list.forEach(item => {
            let imgObj = {};
            imgObj.fileId = item;
            imgObj.imgSrc = 'http://localhost:8888/files/download/' + item;
            swiperList.push(imgObj);
          });
        }
        if (swiperList.length === 0) {
          swiperList.push({imgSrc:"../../imgs/default.png"});
          swiperList.push({imgSrc:"../../imgs/default.png"});
        }
        this.setData({
          obj,
          swiperList
        })
      } else {
        wx.showToast({
          title: res.msg,
          icon: 'none'
        })
      }
    })
  },
  getComment(goodsId) {
    request({url:"/commentInfo/all/" + goodsId}).then(res => {
      if (res.code === "0") {
        let list = res.data;
        list.forEach(item => {
          item.url = '../../imgs/head.jpg';
        })
        this.setData({
          commentList: res.data
        })
      } else {
        wx.showToast({
          title: res.msg,
          icon: 'error'
        })
      }
    })
  },
  handleCartAdd() {
    let user = wx.getStorageSync("user");
    let url = '/pages/login/index?isTabBar=0&url=/pages/goodsInfo/index$id-' + this.data.goodsId;
    console.log(url)
    if (!user) {
      wx.navigateTo({
        url: url,
      })
      return;
    }
    let data = {userId: user.id, level: user.level, goodsId:this.data.goodsId, count: 1};
    request({url:"/cartInfo", data:data, method:"POST"}).then(res => {
      if (res.code === "0") {
        wx.showToast({
          title:"顺利下单"
        })
      } else {
        wx.showToast({
          title: res.msg,
          icon: "error"
        })
      }
    })
  },
  handleCollect() {
    let user = wx.getStorageSync("user");
    let url = '/pages/login/index?isTabBar=0&url=/pages/goodsInfo/index$id-' + this.data.goodsId;
    console.log(url)
    if (!user) {
      wx.navigateTo({
        url: url,
      })
      return;
    }
    let data = {userId: user.id, level: user.level, foreignId:this.data.goodsId};
    request({url:"/collectInfo", data:data, method:"POST"}).then(res => {
      if (res.code === "0") {
        wx.showToast({
          title:"收藏成功"
        })
      } else {
        wx.showToast({
          title: res.msg,
          icon: "error"
        })
      }
    })
  },
})