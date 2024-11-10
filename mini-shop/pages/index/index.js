import {request} from "../../request/index.js";
import {config} from '../../request/config';

Page({
    data: {
        defaultImageUrl: '../../imgs/default.png',
        //例如 xxxList: [],
		goodsInfoCarouselList: [],
		goodsInfoRecommendList: [],
		goodsInfoHotList: [],
		goodsInfoGoodsList: [],

    },
    onLoad: function () {
        //例如 this.getXxxList();
		this.getGoodsInfoCarouselList();
		this.getGoodsInfoRecommendList();
		this.getGoodsInfoHotList();
		this.getGoodsInfoGoodsList();

    },
    search() {
        wx.navigateTo({url: '/pages/search/index'})
    },
/* 获取轮播图开始 */
getGoodsInfoCarouselList() {
    request({url: '/goodsInfoCarousel/page/all'}).then(res => {
        if(res.code === '0') {
            let goodsInfoCarouselList = res.data.list;
            if (!goodsInfoCarouselList || goodsInfoCarouselList.length === 0) {
                goodsInfoCarouselList.push({"name": "名称1", "url": this.data.defaultImageUrl});
                goodsInfoCarouselList.push({"name": "名称2", "url": this.data.defaultImageUrl});
                goodsInfoCarouselList.push({"name": "名称3", "url": this.data.defaultImageUrl});
                goodsInfoCarouselList.push({"name": "名称4", "url": this.data.defaultImageUrl});
            } else {
                if(goodsInfoCarouselList && goodsInfoCarouselList.length > 4) {
                    goodsInfoCarouselList = goodsInfoCarouselList.slice(0, 4);
                }
                goodsInfoCarouselList.forEach(item => {
                    if(!item.fileId || item.fileId === '[]') {
                        item.url = this.data.defaultImageUrl;
                    } else {
                        let fileArr = JSON.parse(item.fileId)
                        item.url = config.baseFileUrl + fileArr[0];
                    }
                });
            }
            this.setData({
                goodsInfoCarouselList
            })
        } else {
            wx.showToast({
                title: res.msg,
                icon: 'none'
            })
        }
    })
},
/* 获取轮播图结束 */
/* 获取推荐商品开始 */
getGoodsInfoRecommendList() {
    request({url: '/goodsInfoRecommend/page/all'}).then(res => {
        if(res.code === '0') {
            let goodsInfoRecommendList = res.data.list;
            if (!goodsInfoRecommendList || goodsInfoRecommendList.length === 0) {
                goodsInfoRecommendList.push({"name": "名称1", "url": this.data.defaultImageUrl});
                goodsInfoRecommendList.push({"name": "名称2", "url": this.data.defaultImageUrl});
                goodsInfoRecommendList.push({"name": "名称3", "url": this.data.defaultImageUrl});
            } else {
                /*--allif(goodsInfoRecommendList && goodsInfoRecommendList.length > 6) {
                    goodsInfoRecommendList = goodsInfoRecommendList.slice(0, 6);
                }all-*/
                goodsInfoRecommendList.forEach(item => {
                    if(!item.fileId || item.fileId === '[]') {
                        item.url = this.data.defaultImageUrl;
                    } else {
                        let fileArr = JSON.parse(item.fileId)
                        item.url = config.baseFileUrl + fileArr[0];
                    }
                });
            }
            this.setData({
                goodsInfoRecommendList
            })
        } else {
            wx.showToast({
                title: res.msg,
                icon: 'none'
            })
        }
    })
},
/* 获取推荐商品结束 */
/* 获取热卖商品开始 */
getGoodsInfoHotList() {
    request({url: '/goodsInfoHot/page/all'}).then(res => {
        if(res.code === '0') {
            let goodsInfoHotList = res.data.list;
            if (!goodsInfoHotList || goodsInfoHotList.length === 0) {
                goodsInfoHotList.push({"name": "名称1", "url": this.data.defaultImageUrl});
                goodsInfoHotList.push({"name": "名称2", "url": this.data.defaultImageUrl});
                goodsInfoHotList.push({"name": "名称3", "url": this.data.defaultImageUrl});
            } else {
                /*--allif(goodsInfoHotList && goodsInfoHotList.length > 6) {
                    goodsInfoHotList = goodsInfoHotList.slice(0, 6);
                }all-*/
                goodsInfoHotList.forEach(item => {
                    if(!item.fileId || item.fileId === '[]') {
                        item.url = this.data.defaultImageUrl;
                    } else {
                        let fileArr = JSON.parse(item.fileId)
                        item.url = config.baseFileUrl + fileArr[0];
                    }
                });
            }
            this.setData({
                goodsInfoHotList
            })
        } else {
            wx.showToast({
                title: res.msg,
                icon: 'none'
            })
        }
    })
},
/* 获取热卖商品结束 */
/* 获取本站商品开始 */
getGoodsInfoGoodsList() {
    request({url: '/goodsInfoGoods/page/all'}).then(res => {
        if(res.code === '0') {
            let goodsInfoGoodsList = res.data.list;
            if (!goodsInfoGoodsList || goodsInfoGoodsList.length === 0) {
                goodsInfoGoodsList.push({"name": "名称1", "url": this.data.defaultImageUrl});
                goodsInfoGoodsList.push({"name": "名称2", "url": this.data.defaultImageUrl});
                goodsInfoGoodsList.push({"name": "名称3", "url": this.data.defaultImageUrl});
                goodsInfoGoodsList.push({"name": "名称4", "url": this.data.defaultImageUrl});
            } else {
                /*--allif(goodsInfoGoodsList && goodsInfoGoodsList.length > 4) {
                    goodsInfoGoodsList = goodsInfoGoodsList.slice(0, 4);
                }all-*/
                goodsInfoGoodsList.forEach(item => {
                    if(!item.fileId || item.fileId === '[]') {
                        item.url = this.data.defaultImageUrl;
                    } else {
                        let fileArr = JSON.parse(item.fileId)
                        item.url = config.baseFileUrl + fileArr[0];
                    }
                });
            }
            this.setData({
                goodsInfoGoodsList
            })
        } else {
            wx.showToast({
                title: res.msg,
                icon: 'none'
            })
        }
    })
},
/* 获取本站商品结束 */

});
