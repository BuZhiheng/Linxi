package cn.linxi.iu.com.model;
import java.io.Serializable;
/**
 * Created by buzhiheng on 2016/8/17.
 *
 * Desc 登录返回user
 */
public class User implements Serializable{
    public Integer user_id;//用户ID
    public Integer operat_id;//加油站操作员ID
    public String station_id;//加油站ID

    public Integer is_bind;//是否绑定过手机号(0:未绑定，1：已绑定)
    public Integer pay_is_bind;//是否设置过支付密码(0:未绑定，1：已绑定)
    public String hash;//签名字符串
    public String im_token;//腾讯IMsignToken
    public Integer user_type;//0：买家 1：加油站 2 : 老板
    public String account;//用户手机号或者爱油号
    public String login_type;//登录类型(0:手机号，1:QQ，2:微信)
    public String openid;//第三方登录唯一标识

    public String mobile;//用户手机号
    public String user_name;//爱油号
    public String code;//用户短信验证码
    public String password;
    public String repassword;
    public String timestamp;
    public String sign;
    public String ver;

    public Boolean is_first;
    public String share_url;
    public String share_title;
    public String share_desc;
}