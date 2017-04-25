package cn.linxi.iu.com.view.iview;
import cn.linxi.iu.com.model.OrderDetail;
/**
 * Created by buzhiheng on 2016/8/22.
 */
public interface IOrderDetailView {
    void showToast(String toast);
    void setOrder(OrderDetail order);
    void setPayTv(String payTv);
    void setBalance(int id);
    void setAlipay(int id);
    void setWxpay(int id);
    void setBalanceCantPay();
    void showPayPsdDialog();
    void showNotBindPayPsd();
    void showNotBindPhone();
    void payOrderSuccess();
    void cancelOrderSuccess();
}