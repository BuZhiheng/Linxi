package cn.linxi.iu.com.model;
import com.google.gson.JsonElement;
/**
 * Created by buzhiheng on 2016/11/9.
 */
public class BossMsg {
    public String profit_amount;
    public BossTodayAmount today_amount;
    public BossTodayPurchase today_purchase;
    public JsonElement user_list;
}