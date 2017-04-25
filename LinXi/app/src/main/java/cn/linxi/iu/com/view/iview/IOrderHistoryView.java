package cn.linxi.iu.com.view.iview;
import java.util.List;
import cn.linxi.iu.com.model.HistoryOrder;
/**
 * Created by buzhiheng on 2016/8/22.
 */
public interface IOrderHistoryView {
    void showToast(String toast);
    void setOrderList(List<HistoryOrder> list);
    void setNoData();
}