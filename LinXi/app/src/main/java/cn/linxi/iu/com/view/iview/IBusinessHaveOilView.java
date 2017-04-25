package cn.linxi.iu.com.view.iview;
import java.util.List;

import cn.linxi.iu.com.model.BusinessOrderCreated;
import cn.linxi.iu.com.model.CustomerOilCard;
/**
 * Created by buzhiheng on 2016/9/1.
 */
public interface IBusinessHaveOilView {
    void showToast(String toast);
    void getCustomerMsgSuccess(List<CustomerOilCard> list);
    void orderCreatedSuccess(BusinessOrderCreated orderCreated);
    void customerHaveNoOilCard(String err);
    void havanoOil();
}