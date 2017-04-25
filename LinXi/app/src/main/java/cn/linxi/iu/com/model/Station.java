package cn.linxi.iu.com.model;
import com.google.gson.JsonElement;
/**
 * Created by buzhiheng on 2016/7/29.
 *
 * Desc 加油站详情
 */
public class Station {
    public String station_id;//加油站ID
    public String name;//加油站名字
    public String avatar;//头像地址
    public String tel;//联系电话
    public String model;//油的类型
    public Double longitude;//经度
    public Double latitude;//纬度
    public String address;//
    public String type_desc;//地址
    public JsonElement details;//加油站油品列表
    public String distance;//距离
    public String city;
    public String cid;//集团id
    public String oil;//是否售油
    public String goods;//是否售汽配
    public String wash;//是否洗车
    public JsonElement model_list;
    public JsonElement rebate_list;
}