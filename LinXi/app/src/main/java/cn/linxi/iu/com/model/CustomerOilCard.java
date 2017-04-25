package cn.linxi.iu.com.model;
/**
 * Created by buzhiheng on 2016/9/1.
 *
 * Desc 加油站扫描客户二维码,加油站端显示客户油卡列表item
 */
public class CustomerOilCard {
    public String user_id;//加油者Id
    public String purchase;//油量
    public String details_id;//油品Id
    public String oil_type;//油的类型,93# 95#
    public String price;//油价
    public String card_id;//
    public String channel;//
    public String type;//类型,"1"汽油,"2"液化气
}