package cn.linxi.iu.com.view.iview;
import java.util.List;
import cn.linxi.iu.com.model.SaleOilCard;
/**
 * Created by buzhiheng on 2016/8/18.
 */
public interface IMyOilCardView {
    void showToast(String toast);
    void refreshRv(List<SaleOilCard> list);
    void setNoData();
}
