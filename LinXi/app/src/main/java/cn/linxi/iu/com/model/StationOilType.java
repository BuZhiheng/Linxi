package cn.linxi.iu.com.model;
import java.io.Serializable;
/**
 * Created by buzhiheng on 2016/8/19.
 *
 * Desc 加油站油品,
 */
public class StationOilType implements Serializable{
    //加油站油品
    public String details_id;
    public String station_id;
    public String oil_type;
    public String price;
    public String type;//1汽油,2液化气
    public String purchase;//加油站预加油输入油量
    public String max_purchase;
}