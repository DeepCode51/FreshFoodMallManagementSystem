import {request} from "../../request/index.js";
Page({

  /**
   * 页面的初始数据
   */
  data: {
    status: "",
    dataList: []
  },

  onLoad: function (options) {
    const status = options.status;
    this.setData({
      status: status
    }),
    this.getOrderData(status);
  },

  getOrderData(status) {
    let user = wx.getStorageSync('user');
    let url = "/orderInfo/page/front?pageNum=1&pageSize=100&userId=" + user.id;
    if (status !== "all") {
      url = url + "&status=" + status;
    }
    request({url: url}).then(res => {
      if (res.code === "0") {
        let list = res.data.list;
        console.log(list)
        list.forEach((item, index) => {
          let goodsInfo = item.goodsList[0];
          let imgSrc = "../../imgs/default.png";
          if (goodsInfo.fileIds) {
            let fileIds = JSON.parse(goodsInfo.fileIds);
            if (fileIds.length) {
                imgSrc = 'http://localhost:8888/files/download/' + fileIds[0];
            }
          }
          item.url = imgSrc;
          item.count = item.goodsList.length;
          item.description = goodsInfo.description
        })
        this.setData({
          dataList: list
        })
      } else {
        console.log(res.data.msg)
      }
    })
  }, 
  deleteOrder(e) {
    let orderId = e.currentTarget.dataset.id;
    let _this = this;
    wx.showModal({
      title: "提示",
      content: "确定删除吗？",
      success(res) {
        if (res.confirm) {
          request({url:"/orderInfo/" + orderId, method: "DELETE"}).then(res => {
            if (res.code === "0") {
              wx.showToast({
                title: '删除成功',
              })
              _this.getOrderData(_this.data.status);
            } else {
              wx.showToast({
                title: res.msg,
                icon: 'error'
              })
            }
          })
        }
      }
    })
  }, 

  payGoods(e) {
    let orderId = e.currentTarget.dataset.id;
    let status = e.currentTarget.dataset.status;
    request({url:"/orderInfo/status/" + orderId + "/" + status, method:"POST"}).then(res => {
      if (res.code === "0") {
        wx.showToast({
          title: '操作成功',
        })
    
        this.getOrderData('all');
      
      } else {
        wx.showToast({
          title: res.msg,
          icon: "none"
        })
      }
    })
  }
})