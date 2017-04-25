package cn.linxi.iu.com.presenter.ipresenter;
import android.widget.EditText;
import cn.linxi.iu.com.model.StationOilType;
/**
 * Created by buzhiheng on 2016/10/25.
 */
public interface IBusinessPreSaleOilPresenter {
    void getOilType();
    void checkPreSale(EditText etPurchase, StationOilType selecedPrice);
    void toPreSale(EditText etPurchase, StationOilType selecedPrice);
    void getPriceAndCount(StationOilType oil,EditText etMoney);
}