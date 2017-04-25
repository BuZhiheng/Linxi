package cn.linxi.iu.com.view.iview;
import java.util.List;
import cn.linxi.iu.com.model.StationOilType;
/**
 * Created by buzhiheng on 2016/10/25.
 */
public interface IBusinessPreSaleOilView {
    void showToast(String toast);
    void setOilType(List<StationOilType> oilType);
    void saleSuccess();
    void setPrice(String price,String amount);
    void showSureDialog(String id,String money);
}