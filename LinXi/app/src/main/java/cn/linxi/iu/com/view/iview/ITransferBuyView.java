package cn.linxi.iu.com.view.iview;
import cn.linxi.iu.com.model.Order;
import cn.linxi.iu.com.model.SaleOilCard;
import cn.linxi.iu.com.model.StationOilType;
import cn.linxi.iu.com.model.TransferBuyCaculate;

/**
 * Created by buzhiheng on 2017/5/12.
 */
public interface ITransferBuyView {
    void showToast(String toast);
    void setData(SaleOilCard sale);
    void addOilType(StationOilType stationOilType, int i);

    void setCaculate(TransferBuyCaculate caculate);

    void orderSuccess(Order order);
}