package cn.linxi.iu.com.model;
/**
 * Created by buzhiheng on 2016/8/1.
 *
 * Desc 微信登录构造类
 */
public class WXToken {
    public String access_token;//接口调用凭证
    public String refresh_token;//用户刷新access_token
    public String openid;//授权用户唯一标识
    public String scope;//用户授权的作用域，使用逗号（,）分隔
    public Integer expires_in;//access_token接口调用凭证超时时间，单位（秒）
}