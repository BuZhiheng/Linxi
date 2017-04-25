package cn.linxi.iu.com.view.iview;
import java.util.List;
import cn.linxi.iu.com.model.SaleOilCard;
/**
 * Created by buzhiheng on 2016/8/25.
 */
public interface ISaleView {
    void showToast(String toast);
    void setSaleOilCard(List<SaleOilCard> list);
    void setTotal(String total);
    void noneOilCard(String err);
    void haveOilCard();
    void haveNoOpen();
    void timeOut();
}