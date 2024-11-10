import {request} from "../../request/index.js";

Page({
    data: {
        defaultImageUrl: '../../imgs/default.png',
        // 左侧的菜单数据
        leftMenuList: [],
        // 右侧的商品数据
        rightContent: [],
        goodsInfoList: [],
        // 被点击的左侧的菜单
        currentIndex: 1,
        // 右侧内容的滚动条距离顶部的距离
        scrollTop: 0
    },
    // 接口的返回数据
    Cates: [],

    onLoad: function (options) {
        this.getSwiperList();
        this.getCates();
        this.getGoodsList(1);
    },
    // 获取分类数据
    getCates() {
        request({url: '/typeInfo/page/all'}).then(res => {
            if (res.code === '0') {
                this.setData({
                    leftMenuList: res.data.list
                })
            }
        })
    },
    getGoodsList(goodId) {
        request({url: '/goodsInfo/findByType/' + goodId}).then(res => {
            if (res.code === '0') {
                let list = res.data;
                list.forEach((item, index) => {
                    let imgSrc = this.data.defaultImageUrl;
                    if (item.fileIds) {
                        let fileId = JSON.parse(item.fileIds)[0];
                        imgSrc = 'http://localhost:8888/files/download/' + fileId;
                    }
                    item.imgSrc = imgSrc;
                })
                this.setData({
                    rightContent: list
                })
            }
        })
    },
    getSwiperList() {
        request({url: '/goodsInfoCarousel/page/all'}).then(res => {
            if (res.code === '0') {
                let swiperList = res.data.list;
                if (!swiperList && swiperList.length === 0) {
                    swiperList.push({imgSrc: this.data.defaultImageUrl});
                    swiperList.push({imgSrc: this.data.defaultImageUrl});
                } else {
                    if (swiperList && swiperList.length > 3) {
                        swiperList = swiperList.slice(0, 3);
                    }
                    swiperList.forEach(item => {
                        if (!item.fileIds || item.fileIds === '[]') {
                            item.url = this.data.defaultImageUrl;
                        } else {
                            let fileArr = JSON.parse(item.fileIds);
                            item.url = 'http://localhost:8888/files/download/' + fileArr[0];
                        }
                    });
                }
                this.setData({
                    goodsInfoList: swiperList
                })
            } else {
                wx.showToast({
                    title: res.msg,
                    icon: 'none'
                })
            }
        })
    },
    // 左侧菜单的点击事件
    handleItemTap(e) {
        /*
        1 获取被点击的标题身上的索引
        2 给data中的currentIndex赋值就可以了
        3 根据不同的索引来渲染右侧的商品内容
         */
        const {id} = e.currentTarget.dataset;

        let rightContent = this.getGoodsList(id);
        this.setData({
            currentIndex: id,
            rightContent: rightContent,
            // 重新设置 右侧内容的scroll-view标签的距离顶部的距离
            scrollTop: 0
        })

    }
})