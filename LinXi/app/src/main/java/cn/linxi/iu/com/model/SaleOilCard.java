package cn.linxi.iu.com.model;
import java.io.Serializable;
/**
 * Created by buzhiheng on 2016/8/25.
 *
 * Desc 卖油item,我的油卡item
 */
public class SaleOilCard implements Serializable{
    public Integer id;
    public Integer details_id;
    public String station_id;
    public Float purchase;
    public String oil_type;
    public String name;
    public String avatar;
    public Double longitude;
    public Double latitude;
    public Float balance;
    public String type;
}