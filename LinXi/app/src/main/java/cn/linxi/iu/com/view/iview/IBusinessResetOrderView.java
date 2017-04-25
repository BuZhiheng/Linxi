package cn.linxi.iu.com.view.iview;
import java.util.List;
import cn.linxi.iu.com.model.BusinessResetOrder;
import cn.linxi.iu.com.model.CustomerOilCard;
/**
 * Created by buzhiheng on 2016/9/1.
 */
public interface IBusinessResetOrderView {
    void showToast(String toast);
    void getCustomerOrderSuccess(BusinessResetOrder order,List<CustomerOilCard> list);
    void orderCreatedSuccess();
    void customerHaveNoOilCard(String err);
}