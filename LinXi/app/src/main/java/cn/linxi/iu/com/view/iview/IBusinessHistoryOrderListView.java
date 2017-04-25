package cn.linxi.iu.com.view.iview;
import java.util.List;
import cn.linxi.iu.com.model.BusinessHistoryOrder;
/**
 * Created by buzhiheng on 2016/9/7.
 */
public interface IBusinessHistoryOrderListView {
    void showToast(String toast);
    void getOrderListSuccess(List<BusinessHistoryOrder> list);
    void setNoData();
}