import {request} from "../../request/index.js";
Page({
  data: {
    isLogin: 0,
    userinfo:{},
    price:0
  },
  onShow(){
    let user = wx.getStorageSync('user');
    if (user) {
      this.setData({
        isLogin: 1,
        userinfo: user
      })
    }
    request({url:"/userInfo/" + user.id}).then(res => {
      if (res.code === "0") {
        this.setData({
          price: res.data.account
        })
      }
    })
  },
  recharge() {
    let user = wx.getStorageSync('user');
    if (!user) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      })
    } else {
      // 跳转到 充值页面
      wx.navigateTo({
        url: '/pages/pay/index'
      });
    }
  },
  navigateToOrder(e) {
    let user = wx.getStorageSync('user');
    if (!user) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      })
    } else {
      let status = e.currentTarget.dataset.status;
      // 跳转到 订单页面
      wx.navigateTo({
        url: '/pages/orderInfo/index?status=' + status
      });
    }
  }
})