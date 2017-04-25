package cn.linxi.iu.com.model;
import com.google.gson.JsonElement;
/**
 * Created by buzhiheng on 2017/4/17.
 */
public class OrderOil {
    public String station_id;
    public String name;
    public String paid_balance;//储值卡剩余余额
    public String paid_amount;//储值卡可支付金额
    public String pay_amount;//最后实付
    public JsonElement list;
}