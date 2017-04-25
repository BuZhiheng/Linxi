package cn.linxi.iu.com.view.iview;
import cn.linxi.iu.com.model.TIModelCustomerOrderSure;
/**
 * Created by buzhiheng on 2016/9/9.
 */
public interface ICustomerOrderSureView {
    void showToast(String toast);
    void setIntentOrder(TIModelCustomerOrderSure order);
    void orderConfirmSuccess();
}
