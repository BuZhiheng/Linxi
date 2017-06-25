package cn.linxi.iu.com.view.iview;
import java.util.Map;

import cn.linxi.iu.com.model.Order;
import cn.linxi.iu.com.model.OrderGoods;
import cn.linxi.iu.com.model.OrderOil;
/**
 * Created by buzhiheng on 2017/4/17.
 */
public interface IOrderView {
    void showToast(String toast);
    void setOrderId(String orderId);
    void setAlipay(int id);
    void setWxpay(int id);
    void setTotalAmount(String s);
    void addItemOil(OrderOil orderOil);
    void addItemGoods(OrderGoods orderGoods);
    void setOilEmpty();
    void setGoodsEmpty();
    void aliPayResult(Map<String, String> stringStringMap);
    void showBalance(Order order);
    void setBalancePay(int ic_station_check);
    void showPsdDialog();

    void showNotBindPhone();

    void showNotBindPayPsd();

    void payOrderSuccess();

    void setAliGone();
    void setAliShow();
}