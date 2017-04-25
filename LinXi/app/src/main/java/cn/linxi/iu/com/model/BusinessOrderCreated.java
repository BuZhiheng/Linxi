package cn.linxi.iu.com.model;
/**
 * Created by buzhiheng on 2016/9/5.
 *
 * Desc 加油站扫码以后跳转的页面,
 * 选取油品,油量生成订单推送给客户端
 */
public class BusinessOrderCreated {
    public String out_trade_no;
    public String create_time;
    public String avatar;
    public String name;
    public String amount;
    public String balance;
    public Float longitude;
    public Float latitude;
}