
define(['commonClass/commonTools','Lib/md5'], function(Tools,MD5){
    var IndexModel = DMJS.DMJSModel.extend({
        defaults: {
        },
        'commonData':{},
        'indexStatic':function(param){//首页统计
        	var urlKey = "indexStatic";
            this.commonRequest(_.extend({'urlKey': urlKey}, param));
        },
        'getPropertyName':function(param){
        	var urlKey = "index.getPropertyName";
            this.commonRequest(_.extend({'urlKey': urlKey}, param));
        },
        "addList":function(param){//首页banner
        	var urlKey="index.addList";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        "noticeList":function(param){//首页公告
        	var urlKey="index.noticeList";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        "tjBidList":function(param){//首页标
        	var urlKey="index.tjBidList";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        "bidList":function(param){//项目详情列表
        	var urlKey="index.bidList";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        "bidDetail":function(param){//项目详情
        	var urlKey="index.bidDetail";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        "bidItem":function(param){//标的附加信息
        	var urlKey="index.bidItem";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        
        "bidRecordsList":function(param){//标的投资记录
        	var urlKey='index.bidRecordsList';
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        "repayList":function(param){//标的还款计划
        	var urlKey='index.repayList';
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        "unUseAwardList":function(param){//标的奖励
        	var urlKey='index.unUseAwardList';
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        "buyBid":function(param){//购买标
        	var urlKey='index.buyBid';
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        'tyjAmountLoan':function(param){//	体验金收益
        	var urlKey='index.tyjAmountLoan';
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        "creditorList":function(param){//债权详情列表
        	var urlKey="index.creditorList";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        "creditorDetail":function(param){//债权投资详情
        	var urlKey="index.creditorDetail";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        "creditorBuy":function(param){//债权购买
        	var urlKey="index.creditorBuy";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //公益标列表
        'gyLoanList':function(param){
        	var urlKey="index.gyLoanList";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //公益标信息(广告、统计信息)
        'gyLoanInfo':function(param){
        	var urlKey="index.gyLoanInfo";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //公益标详情
        'gyLoanItem':function(param){
        	var urlKey="index.gyLoanItem";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //公益标捐助
        'gyLoanBid':function(param){
        	var urlKey="index.gyLoanBid";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //捐助记录
        'gyLoanRecordsList':function(param){
        	var urlKey="index.gyLoanRecordsList";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //资讯
        articleList:function(param){
        	var urlKey="index.articleList";
        	if(param.data.type=="WZGG")
        	urlKey="index.noticeList";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //资讯详情
        articleItem:function(param){
        	var urlKey="index.articleItem";
        	if(param.data.type=="WZGG")
        	urlKey="index.noticeItem";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //土豪榜
        cashList:function(param){
        	var urlKey="index.getUserBidRank";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //信用贷常用信息
        xyd:function(param){
        	var urlKey="index.xyd";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //申请信用贷需要的认证
        isAuthentication:function(param){
        	var urlKey="index.isAuthen";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
         //信用贷申请
        xydSub:function(param){
        	var urlKey="index.xydSub";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //个人借款
        grSub:function(param){
        	var urlKey="index.grSub";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //企业借款
        qySub:function(param){
        	var urlKey="index.qySub";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //我的推广
        mySpread:function(param){
        	var urlKey="user.mySpread";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
         //运营数据
        operationData:function(param){
        	var urlKey="index.operationData";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
       
        //商品列表
        scoreMallList:function(param){
        	var urlKey="mall.scoreMallList";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //商品详情
        commodityDetails:function(param){
        	var urlKey="mall.commodityDetails";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //加入购物车
        addShoppingCart:function(param){
        	var urlKey="mall.addShoppingCar";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //加入购物车
        shoppingCart:function(param){
        	var urlKey="mall.shoppingCar";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //删除购物车商品
        delShoppingCar:function(param){
        	var urlKey="mall.delShoppingCar";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //商品描述
        showDetail:function(param){
        	var urlKey="mall.showDetail";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //购买商品
        buyGoods:function(param){
        	var urlKey="mall.buyGoods";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //用户信息
        myAccount:function(param){
        	var urlKey="user.myAccount";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //用户收货地址
        myHarvestAddress:function(param){
        	var urlKey="mall.myHarvestAddress";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //增加用户收货地址
        addAddress:function(param){
        	var urlKey="mall.addAddress";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //删除用户收货地址
        deleteAddress:function(param){
        	var urlKey="mall.deleteAddress";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //修改用户收货地址
        updateAddress:function(param){
        	var urlKey="mall.updateAddress";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //查询用户收货地址详情
        getAddressById:function(param){
        	var urlKey="mall.getAddressById";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
         //用户默认收货地址
        getDefaultAddress:function(param){
        	var urlKey="user.getDefaultAddress";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
    });
    return IndexModel;
});
