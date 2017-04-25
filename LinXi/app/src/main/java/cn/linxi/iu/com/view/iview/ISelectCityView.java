package cn.linxi.iu.com.view.iview;
import java.util.List;
import cn.linxi.iu.com.model.SelectCity;
/**
 * Created by buzhiheng on 2016/8/26.
 */
public interface ISelectCityView {
    void showToast(String toast);
    void setCurrentCity(SelectCity currentCity,String city);
    void getCitySuccess(List<SelectCity> list);
}
