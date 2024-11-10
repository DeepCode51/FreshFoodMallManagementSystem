import {request} from "../../request/index.js";
Page({
  data: {
    username: "",
    password: "",
    url: "",
    isTabBar: 0
  },

  onLoad: function (options) {
    let url = options.url.replace("$", "?").replace("-","=");
    let isTabBar = options.isTabBar;
    this.setData({
      username: "",
      password: "",
      url: url,
      isTabBar: isTabBar
    })
  },

  onName(e) {
    this.setData({
      username: e.detail.value
    })
  },

  onPassword(e) {
    this.setData({
      password: e.detail.value
    })
  },

  login(e) {
    let url = this.data.url;
    let isTabBar = this.data.isTabBar;
    let data = {name:this.data.username, password: this.data.password}
    request({url:"/login", data:data, method:"POST"}).then(res => {
      if (res.code === "0") {
        wx.showToast({
          title: '登陆成功',
          mask: true
        })
        // 存到localStorage里
        wx.setStorageSync('user', res.data)
        setTimeout(() => {
          if (isTabBar === "0") {
            wx.navigateTo({
              url: url,
            })
          } else {
            wx.switchTab({
              url: url,
            })
          }
        }, 1500);
      }
    })
  }
})