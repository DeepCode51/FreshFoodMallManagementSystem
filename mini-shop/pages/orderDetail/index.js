import {request} from "../../request/index.js";
Page({
  data: {
    cart: []
  },
  onLoad(options) {
    let id = options.id;
    let status = options.status;
    request({url:'/orderInfo/order/' + id}).then(res => {
      if (res.code === '0') {
        let list = res.data.goodsList;
        list.forEach(item => {
          let imgSrc = "../../imgs/default.png";
          if (item.fileIds) {
            let fileIds = JSON.parse(item.fileIds);
            if (fileIds.length) {
                imgSrc = 'http://localhost:8888/files/download/' + fileIds[0];
            }
          }
          item.url = imgSrc
        })
        this.setData({
          cart: list,
          status: status
        })
      }
    })
  },
  // 商品的选中
  handeItemChange(e) {

  },
  // 商品数量的编辑功能
handleItemNumEdit(e) {
    // 1 获取传递过来的参数 
    const { operation, id } = e.currentTarget.dataset;
    // 2 获取购物车数组
    let { cart } = this.data;
    // 3 找到需要修改的商品的索引
    const index = cart.findIndex(v => v.id === id);
    // 4 判断是否要执行删除
    if (cart[index].num === 1 && operation === -1) {
      // 4.1 弹窗提示
      wx.showModal({
        content: '您是否要删除？',
        success: (res) => {
          if (res.confirm) {
            cart.splice(index, 1);
            // 注意，在事件内部，必须在这里设置cart，在外面设置无效
            this.setData({
              cart
            })
          }
        }
      })
    } else {
      // 4  进行修改数量
      cart[index].num += operation;
    }
    this.setData({
      cart
    })
    
  },
  // 点击 结算 
  handlePay(){

    // 3 跳转到 支付页面
    wx.navigateTo({
      url: '/pages/order/index'
    });
      
  },
  comment(e) {
    let goodsId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/comment/index?goodsId=' + goodsId,
    })
  },
})