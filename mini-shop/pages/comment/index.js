import {request} from "../../request/index.js";
Page({

  data: {
    goodsId: 0,
    content: ''
  },


  onLoad: function (options) {
    console.log(options)
    this.setData({
      goodsId: options.goodsId
    })
  },

  onComment(e) {
    this.setData({
      content: e.detail.value
    })
  },

  comment(e) {
    let user = wx.getStorageSync('user');
    let data = {userId: user.id, content: this.data.content, goodsId: this.data.goodsId};
    request({url:"/commentInfo", data:data, method:"POST"}).then(res => {
      if (res.code === "0") {
        wx.showToast({
          title: '评论成功',
        })
        wx.switchTab({
          url: '/pages/user/index',
        })
      } else {
        wx.showToast({
          title: res.msg,
          icon: "none"
        })
      }
    })
  }
})