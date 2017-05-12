package cn.linxi.iu.com.view.iview;
import cn.linxi.iu.com.model.SaleOilCard;
import cn.linxi.iu.com.model.StationOilType;
/**
 * Created by buzhiheng on 2017/5/12.
 */
public interface ITransferBuyView {
    void showToast(String toast);
    void setData(SaleOilCard sale);
    void addOilType(StationOilType stationOilType, int i);
}