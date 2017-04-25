package cn.linxi.iu.com.model;
import java.io.Serializable;
/**
 * Created by buzhiheng on 2016/9/8.
 *
 * Desc TIM 加油站输入油品等确认订单,
 * 推送给客户端确认类
 */
public class TIModelCustomerOrderSure implements Serializable{
    public String name;
    public Float amount;
    public String purchase;
    public String out_trade_no;
}