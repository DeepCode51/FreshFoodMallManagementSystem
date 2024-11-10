import {request} from '../../request/index.js'

Page({
  data: {
    defaultImageUrl: '../../imgs/default.png',
    cart: [ ],
    totalPrice: 0,
    totalNum: 0
  },
  onShow(e) {
    this.getCartInfo();
  },
  // 商品的选中
  getCartInfo() {
    let user = wx.getStorageSync("user");
    let url = '/pages/login/index?isTabBar=1&url=/pages/cartInfo/index';
    if (!user) {
      wx.navigateTo({
        url: url,
      })
      return;
    }
    request({url: '/cartInfo?userId=' + user.id}).then(res => {
      if (res.code === '0') {
        console.log(res.data);
        let cartList = res.data;
        let totalPrice = 0;
        let totalNum = 0;
        cartList.forEach(item => {
          totalNum += item.count;
          totalPrice += item.count * item.price * item.discount;
          let imgSrc = this.data.defaultImageUrl;
          if (item.fileIds) {
            let fileId = JSON.parse(item.fileIds)[0];
            imgSrc = 'http://localhost:8888/files/download/' + fileId;
          }
          item.url = imgSrc;
        })
        this.setData({
          cart: cartList,
          totalNum: totalNum,
          totalPrice: totalPrice.toFixed(2)
        })
        console.log(this.data.cart)
      }
    })
  },
  // 商品数量的编辑功能
  handleItemNumEdit(e) {
    // 1 获取传递过来的参数 
    const { operation, id } = e.currentTarget.dataset;
    // 2 获取购物车数组
    let cart = this.data.cart;
    // 3 找到需要修改的商品的索引
    const index = cart.findIndex(v => v.id === id);
    // 4 判断是否要执行删除
    if (cart[index].count === 1 && operation === -1) {
      // 4.1 弹窗提示
      wx.showModal({
        content: '您是否要删除？',
        success: (res) => {
          if (res.confirm) {
            let user = wx.getStorageSync("user");
            request({url: '/cartInfo/goods/' + user.id + '/' + id, method: 'DELETE'}).then(res => {
              if (res.code === '0') {
                let cart = this.data.cart;
                cart.splice(index, 1);
                let totalPrice = 0;
                let totalNum = 0;
                cart.forEach(item => {
                  totalNum += item.count;
                  totalPrice += item.count * item.price * item.discount;
                })
                this.setData({
                  cart: cart,
                  totalPrice: totalPrice.toFixed(2),
                  totalNum: totalNum
                })
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
    } else {
      // 4  进行修改数量
      let cart = this.data.cart;
      cart[index].count += operation;
      // 重新计算一下总价
      let totalPrice = 0;
      let totalNum = 0;
      cart.forEach(item => {
        totalNum += item.count;
        totalPrice += item.count * item.price * item.discount;
      })
      this.setData({
        cart: cart,
        totalPrice: totalPrice.toFixed(2),
        totalNum: totalNum
      })
    }
  },
  // 点击 结算 
  handlePay(){
    if (this.data.cart.length === 0) {
      wx.showToast({
        title:"购物车空空如也~",
        icon: "none"
      })
      return;
    }
    let user = wx.getStorageSync("user");
    let data = {userId: user.id, level: user.level, totalPrice: this.data.totalPrice, goodsList: this.data.cart};
    request({url: '/orderInfo', method: 'POST', data:data}).then(res => {
      if (res.code === '0') {
        wx.showToast({
          title:"加入购物车成功"
        })
        // 3 跳转到 支付页面
        wx.navigateTo({
          url: '/pages/orderInfo/index?status=待付款'
        });
      } else {
        wx.showToast({
          title: res.msg,
          icon: 'error'
        })
      }
    })
      
  }
})