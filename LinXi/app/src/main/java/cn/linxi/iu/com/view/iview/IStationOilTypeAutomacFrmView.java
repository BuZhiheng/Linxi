package cn.linxi.iu.com.view.iview;
import java.util.List;
import cn.linxi.iu.com.model.Automac;
import cn.linxi.iu.com.model.AutomacBanner;
import cn.linxi.iu.com.model.AutomacType;
/**
 * Created by buzhiheng on 2017/3/6.
 */
public interface IStationOilTypeAutomacFrmView {
    void showToast(String toast);
    void setAutomacType(List<AutomacType> list);
    void setAutomacData(List<Automac> list);
    void setAutomacBanner(List<AutomacBanner> list);
    void setOilPriceWidth(int width);
    void setSortSale(int sort);
    void setSortPrice(int sort);
}