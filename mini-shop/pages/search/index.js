import {request} from "../../request/index.js";
import {config} from '../../request/config';

Page({
    data: {
       goodsList: [],
       searchValue: ''
    },
    onLoad: function () {
      
    },
    searchValueInput(e) {
      let text = e.detail.value
      if (!text) {
       return
      }
      request({url: '/goodsInfo/searchGoods?text=' + text}).then(res => {
          if(res.code === '0') {
              let goodsList = res.data;
              if (goodsList.length) {
                  goodsList.forEach(item => {
                      if(!item.fileId || item.fileId === '[]') {
                          item.url = this.data.defaultImageUrl;
                      } else {
                          let fileArr = JSON.parse(item.fileId)
                          item.url = config.baseFileUrl + fileArr[0];
                      }
                  });
              }
              this.setData({
                goodsList: goodsList
              })
          } else {
              wx.showToast({
                  title: res.msg,
                  icon: 'none'
              })
          }
      })
  },
});
