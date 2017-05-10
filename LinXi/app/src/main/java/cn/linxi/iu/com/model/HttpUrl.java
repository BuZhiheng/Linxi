package cn.linxi.iu.com.model;
/**
 * Created by buzhiheng on 2016/8/17.
 */
public class HttpUrl {//http://wap.kylinoil.com/app/views/vip-description.html
    public static final String URL_VIP = "http://wap.kylinoil.com/app/views/vip-description.html";
    public static final String URL_AGREEMENT = "http://wap.kylinoil.com/app/views/agreement.html";
    public static final String IP = "http://192.168.1.145";
    public static final String devBaseUrl = "http://dev-api.kylinoil.com";
    public static final String testBaseUrl = "http://test-api.kylinoil.com";
    public static final String realBaseUrl = "http://api.kylinoil.com";
    public static final String finalUrl = devBaseUrl;
    //短信验证码 post
    public static final String getCodeUrl = finalUrl + "/message/code/send";
    //验证验证码 post
    public static final String verifyCodeUrl = finalUrl + "/message/code/verify";
    //用户注册
    public static final String registerUrl = finalUrl + "/user/register/index";
    //找回密码
    public static final String findLoginPsdUrl = finalUrl + "/user/login/reset";
    //绑定手机号
    public static final String bindPhoneUrl = finalUrl + "/user/binding/mobile";
    //绑定银行卡
    public static final String bindUnCardUrl = finalUrl + "/user/binding/bank";
    //绑定支付宝
    public static final String bindAliUrl = finalUrl + "/user/binding/account";
    //绑定推荐人
    public static final String bindInviteUrl = finalUrl + "/user/binding/invitor";
    //用户登录/member/member/login
    public static final String loginUrl = finalUrl + "/member/member/login";
    //个人中心
    public static final String personalCenterUrl = finalUrl + "/user/center/index";
    //红包
    public static final String personalEnvelopUrl = finalUrl + "/activity/envelope";
    //用户初次登录抽红包
    public static final String firstLoginEnvelopUrl = finalUrl + "/member/member/chance";
    //抽奖
    public static final String personalPrizeUrl = finalUrl + "/activity/lottery";
    //更新
    public static final String updateUrl = finalUrl + "/message/version";
    //初始化获取推送订单
    public static final String getTIMOrderUrl = finalUrl + "/message/push/order";
    //初始化TIM签名
    public static final String getTIMSignUrl = finalUrl + "/user/sign/im";
    //修改图片上传地址
    public static final String personalSetPhotoUrl = finalUrl + "/user/center/avatar";
    //修改支付密码
    public static final String changePayPsdUrl = finalUrl + "/password/find/payreset";
    //获取城市列表
    public static final String getCityUrl = finalUrl + "/city/index/list";
    //获取加油站列表
    public static final String getStationListUrl = finalUrl + "/oil/station";
    //获取加油站列表
    public static final String getTodayPriceUrl = finalUrl + "/oil/today/price";
    //获取加油站详情
    public static final String getStationDetailUrl = finalUrl + "/oil/station/data";
    //获取加油站汽配列表
    public static final String getStationAutomacTypeListUrl = finalUrl + "/member/auto";
    //获取加油站汽配详情
    public static final String getStationAutomacTypeDataUrl = finalUrl + "/member/auto/data";
    //添加到购物车
    public static final String addShoppingCarUrl = finalUrl + "/member/cart/add";//
    //更新购物车
    public static final String updateShoppingCarUrl = finalUrl + "/member/cart/update";
    //删除购物车
    public static final String deleteShoppingCarUrl = finalUrl + "/member/cart/delete";
    //批量删除购物车
    public static final String removeShoppingCarUrl = finalUrl + "/member/cart/remove";
    //获取购物车
    public static final String getShoppingCarUrl = finalUrl + "/member/cart";//
    //我的订单列表
    public static final String getOrderListUrl = finalUrl + "/member/order";//
    //订单创建
    public static final String addOrderUrl = finalUrl + "/member/order/create";
    //订单删除
    public static final String removeOrderUrl = finalUrl + "/member/order/remove";
    //客户订单确认
    public static final String addOrderConfirmUrl = finalUrl + "/oil/order/confirm";
    //订单取消
    public static final String cancelOrderUrl = finalUrl + "/oil/order/cancel";
    //订单详情
    public static final String getOrderDetailUrl = finalUrl + "/member/order/data";
    //订单更新
    public static final String updateOrderDetailUrl = finalUrl + "/member/order/modify";
    //卖油列表
    public static final String getSaleOilCardUrl = finalUrl + "/oil/sale";
    //卖油弹框明细
    public static final String getSaleOilCardDataUrl = finalUrl + "/oil/sale/data";
    //卖油
    public static final String saleOilCardUrl = finalUrl + "/oil/sale/confirm";
    //我的油卡
    public static final String getMyOilCardUrl = finalUrl + "/member/card/index";
    //支付宝支付
    public static final String payByAli = finalUrl + "/pay/alipay";
    //微信支付
    public static final String payByWX = finalUrl + "/pay/wxpay";
    //余额支付
    public static final String payByBalance = finalUrl + "/password/verify/pay";
    //充值
    public static final String recharge = finalUrl + "/capital/recharge";
    //充值支付宝支付
    public static final String payByAliRecharge = finalUrl + "/capital/alipay";
    //充值微信支付
    public static final String payByWXRecharge = finalUrl + "/capital/wxpay";
    //充值活动
    public static final String rechargeDiscount = finalUrl + "/activity/discount";
    //提现
    public static final String cash = finalUrl + "/capital/withdrow";
    //获取二维码
    public static final String getQRCode = finalUrl + "/user/center/qrcode";
    //余额明细
    public static final String getBalanceList = finalUrl + "/user/center/balance";
    //反馈
    public static final String feedBack = finalUrl + "/user/feedback";
    //签到
    public static final String userSign = finalUrl + "/user/past";
    //签到列表
    public static final String userSignList = finalUrl + "/user/past/list";
    //签到获得油卡
    public static final String userSignCardList = finalUrl + "/user/past/envelope";
    //油卡转让市场
    public static final String transferSaleMarket = finalUrl + "/member/transfor/market";
    //油卡转让详情
    public static final String oilCardTransferSaleDetail = finalUrl + "/member/card/data";
    //油卡转让创建
    public static final String oilCardTransferSaleCreate = finalUrl + "/member/card/create";
    //加油站登录
    public static final String businessLogin = finalUrl + "/enterprise/login";
    //加油站获取用户信息
    public static final String businessGetUser = finalUrl + "/enterprise/user";
    //加油站获取个人中心信息
    public static final String businessGetUserMine = finalUrl + "/enterprise/user/data";
    //加油站下班结算
    public static final String businessWorkOut = finalUrl + "/enterprise/user/settlement";
    //加油站初始化登录密码
    public static final String businessInitPsd = finalUrl + "/enterprise/password/reset";
    //加油站创建订单
    public static final String businessOrderCreate = finalUrl + "/enterprise/order/create";
    //加油站创建订单微信二維碼
    public static final String businessOrderCreateWX = finalUrl + "/enterprise/formpay";
    //加油站收款列表
    public static final String businessHistoryOrderList = finalUrl + "/enterprise/order/list";
    //加油站获取修改订单
    public static final String businessGetResetUnfinishOrder = finalUrl + "/enterprise/order/data";
    //加油站保存修改订单
    public static final String businessResetUnfinishOrder = finalUrl + "/enterprise/order/save";
    //加油站获取预售油品类型
    public static final String businessPreSale = finalUrl + "/enterprise/advance";
    //加油站获取预售油品类型
    public static final String businessPreOrder = finalUrl + "/enterprise/advance/create";
    //加油站获取推广收益
    public static final String businessGetIncome = finalUrl + "/enterprise/user/balance";
    //加油站出示二维码
    public static final String businessShowQRCode = finalUrl + "/enterprise/advance/qrcode";
    //Boss端获取数据
    public static final String bossGetMsg = finalUrl + "/member/boss";
    //Boss端获取数据
    public static final String bossGetMsgDetail = finalUrl + "/user/boss/data";
}