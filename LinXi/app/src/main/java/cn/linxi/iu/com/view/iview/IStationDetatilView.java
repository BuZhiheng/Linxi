package cn.linxi.iu.com.view.iview;
import java.util.List;

import cn.linxi.iu.com.model.Rebate;
import cn.linxi.iu.com.model.StationOilType;
import cn.linxi.iu.com.model.OrderDetail;
import cn.linxi.iu.com.model.Station;
/**
 * Created by buzhiheng on 2016/8/19.
 */
public interface IStationDetatilView {
    void showToast(String toast);
    void setStation(Station station,List<Rebate> list);
    void setOilModel(List<StationOilType> list);
    void setOilPriceWidth(int width);
    void commitOrderSuccess(OrderDetail order);
    void setOilCout(String cout);
    void callPhone();
    void toNvg();
}