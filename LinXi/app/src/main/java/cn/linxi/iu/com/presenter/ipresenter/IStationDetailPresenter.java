package cn.linxi.iu.com.presenter.ipresenter;
import android.widget.EditText;
import cn.linxi.iu.com.model.StationOilType;
/**
 * Created by buzhiheng on 2016/8/19.
 */
public interface IStationDetailPresenter {
    void getStationDetail();
    void addShoppingCar(StationOilType detail, EditText editText);
    void commitOrder(StationOilType detail,String purchase);
    void oilCoutAdd(EditText etOil);
    void oilCoutSub(EditText etOil);
    void checkPermission(String permission,int code);
}