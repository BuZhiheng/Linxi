package cn.linxi.iu.com.model;
import java.io.Serializable;
/**
 * Created by buzhiheng on 2016/9/7.
 *
 * Desc 加油站加油收款明细item,含未完成和已完成
 */
public class BusinessHistoryOrder implements Serializable {
    public String user_id;//未完成的用户Id
    public String out_trade_no;//未完成的订单号
    public String create_time;
    public String purchase;
    public String user_name;
    public String price;
    public String oil_type;
    public String amount;
}