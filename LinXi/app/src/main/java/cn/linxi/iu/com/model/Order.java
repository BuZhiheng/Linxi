package cn.linxi.iu.com.model;
import com.google.gson.JsonElement;
/**
 * Created by buzhiheng on 2017/4/17.
 */
public class Order {
    public String oid;
    public String source;
    public String balance;
    public String balance_use;
    public String total_amount;
    public String percent;
    public String create_time;
    public String receiver;
    public String tel;
    public String address;
    public String server_time;
    public JsonElement oil_list;
    public JsonElement goods_list;
}