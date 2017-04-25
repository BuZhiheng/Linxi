package cn.linxi.iu.com.model;
/**
 * Created by buzhiheng on 2016/8/18.
 *
 * Desc 第三方登录的基本参数
 */
public class TencentUser {
    //qq 微信共有的返回字段
    public String openid;
    public String nickname;
    public String city;
    public String country;
    public String province;//普通用户个人资料填写的省份
    //微信 独有字段
    public Integer sex;//性别(1为男性，2为女性)
    public String unionid;
    public String headimgurl;
    //qq 独有字段
    public String gender;//性别(“男”|“女”)
    public String figureurl_qq_1;//大小为40×40像素的QQ头像URL
    public String figureurl_qq_2;//大小为100×100像素的QQ头像URL
    public String figureurl_2;//大小为100×100像素的QQ空间头像URL
}