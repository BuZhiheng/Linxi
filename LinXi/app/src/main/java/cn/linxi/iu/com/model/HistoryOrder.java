package cn.linxi.iu.com.model;
import com.google.gson.JsonElement;
import java.io.Serializable;
/**
 * Created by buzhiheng on 2016/8/22.
 *
 * Desc 客户端我的订单item,包括历史订单和未完成订单
 */
public class HistoryOrder implements Serializable{
    public String oid;//订单号ID
    public String create_time;//订单创建时间
    public String amount;//支付总额
    public String express_no;//支付总额
    public String express_name;//支付总额
    public String method;//支付总额
    public JsonElement list;
}