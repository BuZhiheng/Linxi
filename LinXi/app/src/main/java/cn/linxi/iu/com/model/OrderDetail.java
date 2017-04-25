package cn.linxi.iu.com.model;
import java.io.Serializable;
/**
 * Created by buzhiheng on 2016/8/22.
 *
 * Desc 客户端我的订单item,包括历史订单和未完成订单
 */
public class OrderDetail implements Serializable{
    public String oid;//订单号ID
    public String out_trade_no;//订单号ID
    public String create_time;//订单创建时间
    public String address;//加油站地址
    public String desc;//备注
    public String avatar;//头像地址
    public String name;//加油站名称
    public String purchase;//购买油量
    public Float amount;//支付总额
    public Float balance;//账户余额
    public Float longitude;//经度
    public Float latitude;//纬度
    public Integer trade_state;//支付状态 0待支付,1已支付
    public String type;//1汽油|2液化气
    public String oil_type;//93#|CNG
    public String price;//单价
}