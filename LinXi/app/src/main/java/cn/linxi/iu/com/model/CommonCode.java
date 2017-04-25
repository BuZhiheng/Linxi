package cn.linxi.iu.com.model;
/**
 * Created by BuZhiheng on 2016/7/7.
 *
 * Desc App配置,静态全局变量,请谨慎操作
 */
public class CommonCode {
    public static final String APP_ICON = "http://wap.kylinoil.com/wechatImages/appicon@2x.png";
    public static final String APP_TEACH = "http://www.kylinoil.com/html/intraduceApp.html";
    //YXBpLmt5bGlub2lsLmNvbQ==      real
    //d3d3Lmt5bGlub2lsLmNvbQ==      dev
    public static final String APP_KEY = HttpUrl.finalUrl.equals(HttpUrl.realBaseUrl)?"YXBpLmt5bGlub2lsLmNvbQ==":"d3d3Lmt5bGlub2lsLmNvbQ==";
    //APP 第三方key
    public static final String APP_ID_QQ = "1105468605";
    public static final String APP_ID_WX = "wx2466bd17ef0d4f2e";
    public static final String APP_SECRET_WX = "ff61672b2d8ddf9ef62939cf6a86c645";
    public static final String ALI_CLOUD_ENDPOINT = "http://oss-cn-shanghai.aliyuncs.com";
    public static final String ALI_CLOUD_ACCESS_KEYID = "LTAIhMad7ARzuEjt";
    public static final String ALI_CLOUD_ACCESS_KEYSECRET = "sCq8WX7Pub5ncCxlck2eenmB5Hwcbi";
    public static final String ALI_CLOUD_BUCKETNAME = "dev-oil";
    public static final String ALI_CLOUD_PHOTOURL = "/app/photo/";
    public static final String TIM_ACCOUNT_TYPE = "7248";
    //1400014273 real
    //1400014230 dev
    public static final int TIM_APPID = HttpUrl.finalUrl.equals(HttpUrl.realBaseUrl)?1400014273:1400014230;
    //TIM初始化判断来自客户端还是加油站端
    public static final int TIM_INIT_TYPE_CLIENT = 1;
    public static final int TIM_INIT_TYPE_STATION = 2;
    //Activity 跳转携带参数信息
    public static final String INTENT_ORDER_ID = "INTENT_ORDER_ID";
    public static final String INTENT_COMMON = "INTENT_COMMON";
    public static final String INTENT_QRCODE = "INTENT_QRCODE";
    public static final String INTENT_STATION_ID = "INTENT_STATION_ID";
    public static final String INTENT_STATION_CID = "INTENT_STATION_CID";
    public static final String INTENT_STATION_GID = "INTENT_STATION_GID";
    public static final String INTENT_REGISTER_USER = "INTENT_REGISTER_USER";
    public static final String INTENT_BIND_PHONE_FROM = "INTENT_BIND_PHONE_FROM";
    public static final String INTENT_WEBVIEW_URL = "INTENT_WEBVIEW_URL";
    public static final String INTENT_LAT = "INTENT_LAT";
    public static final String INTENT_LNG = "INTENT_LNG";
    //SharedPreferences 存储名称
    public static final String SP_USER_USERID = "SP_USER_USERID";
    public static final String SP_USER_USERNAME = "SP_USER_USERNAME";
    public static final String SP_USER_NICKNAME = "SP_USER_NICKNAME";
    public static final String SP_USER_VIPDESC = "SP_USER_VIPDESC";
    public static final String SP_USER_GAME_RULE = "SP_USER_GAME_RULE";
    public static final String SP_USER_ENVELOP_RULE = "SP_USER_ENVELOP_RULE";
    public static final String SP_USER_PHONE = "SP_USER_PHONE";
    public static final String SP_USER_PHOTO = "SP_USER_PHOTO";
    public static final String SP_USER_PASSWORD = "SP_USER_PASSWORD";
    public static final String SP_USER_BALANCE = "SP_USER_BALANCE";
    public static final String SP_USER_PAYPSDISBIND = "SP_USER_PAYPSDISBIND";
    public static final String SP_USER_PHONEISBIND = "SP_USER_PHONEISBIND";
    public static final String SP_USER_LAST_ALIACCOUNT = "SP_USER_LAST_ALIACCOUNT";
    public static final String SP_USER_LAST_BANKACCOUNT = "SP_USER_LAST_BANKACCOUNT";
    public static final String SP_USER_INVITE_MOBILE = "SP_USER_INVITE_MOBILE";
    public static final String SP_USER_IM_TOKEN = "SP_USER_IM_TOKEN";
    public static final String SP_USER_LAST_LOGIN_TYPE = "SP_USER_LAST_LOGIN_TYPE";
    public static final String SP_USER_USER_IS_VIP = "SP_USER_USER_IS_VIP";//是否为vip
    public static final String SP_APP_VOICE = "SP_APP_VOICE";//声音提示
    public static final String SP_APP_VIB = "SP_APP_VIB";//震动提示
    public static final String SP_APP_IGNORE_VERSION = "SP_APP_IGNORE_VERSION";

    public static final String SP_LOC_CITY = "SP_LOC_CITY";
    public static final String SP_LOC_CITY_CODE = "SP_LOC_CITY_CODE";
    public static final String SP_LOC_CITY_LAST = "SP_LOC_CITY_LAST";
    public static final String SP_LOC_CITY_CODE_LAST = "SP_LOC_CITY_CODE_LAST";
    public static final String SP_LOC_LAT = "SP_LOC_LAT";
    public static final String SP_LOC_LNG = "SP_LOC_LNG";
    public static final String SP_IS_LOGIN = "SP_IS_LOGIN";
    public static final String SP_IS_STARTED = "SP_IS_STARTED";
    public static final String SP_USER_OPERA_ID = "SP_USER_OPERA_ID";
    public static final String SP_USER_STATION_ID = "SP_USER_STATION_ID";
    public static final String SP_IS_LOGIN_BOSS = "SP_IS_LOGIN_BOSS";
    public static final String SP_IS_LOGIN_BUSINESS = "SP_IS_LOGIN_BUSINESS";
    public static final String SP_IS_BUSINESS_PSDISINIT = "SP_IS_BUSINESS_PSDISINIT";
    public static final String SP_IS_BUSINESS_USERNAME = "SP_IS_BUSINESS_USERNAME";
    public static final String SP_IS_BUSINESS_PSD = "SP_IS_BUSINESS_PSD";

    public static final String SP_IS_NEW_USER = "SP_IS_NEW_USER";
    public static final String SP_SHARE_URL = "SP_SHARE_URL";
    public static final String SP_SHARE_TITLE = "SP_SHARE_TITLE";
    public static final String SP_SHARE_DESC = "SP_SHARE_DESC";
    public static final String SP_IS_LOOKED_GIFT = "SP_IS_LOOKED_GIFT";

    public static final String SP_WAIT_IS_OPEN_SALE = "SP_WAIT_OPEN_IS_SALE";
    public static final String SP_WAIT_IS_OPEN_PAST = "SP_WAIT_OPEN_IS_PAST";
    public static final String SP_WAIT_IS_OPEN_ENVELOP = "SP_WAIT_OPEN_IS_ENVELOP";

    //登录方式
    public static final String LOGIN_BY_USERNAME = "0";
    public static final String LOGIN_BY_QQ = "1";
    public static final String LOGIN_BY_WX = "2";
    //支付方式
    public static final int PAY_BY_BALANCE = 0;
    public static final int PAY_BY_ZFB = 1;
    public static final int PAY_BY_WX = 2;
    //请求超时
    public static final int HTTP_TIMEOUT = 20000;
    //refresh 初始化刷新效果
    public static final int OFFSET_START = -100;
    public static final int OFFSET_END = 119;
    //网络请求参数
    public static final String HTTP_CASH_CARDALI = "alipay";
    public static final String HTTP_CASH_CARDBANK = "bank";
    public static final String ORDER_BY_SALE = "saled";
    public static final String ORDER_BY_PRICE = "price";
    public static final String ORDER_BY_DESC = "desc";
    public static final String ORDER_BY_ASC = "asc";
    //activity forresult 返回码
    public static final int ACTIVITY_RESULT_CODE_BUY = 0;
    public static final int ACTIVITY_RESULT_CODE_SALE = 1;
    public static final int ACTIVITY_RESULT_CODE_MINE = 2;
    public static final int ACTIVITY_RESULT_CODE_QRCODE = 3;
    //加油站详情类型
    public static final int STATION_TYPE_OIL = 1;
    public static final int STATION_TYPE_GOODS = 2;
    public static final int STATION_TYPE_BOTH = 3;
    //
    public static final String NOTICE_NETWORK_DISCONNECT = "网络连接失败";
}