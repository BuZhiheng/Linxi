package cn.linxi.iu.com.view.iview;
import cn.linxi.iu.com.model.OrderDetail;
import cn.linxi.iu.com.model.ShoppingCar;
/**
 * Created by buzhiheng on 2017/3/16.
 */
public interface IShoppingCarView {
    void showToast(String toast);
    void addItem(ShoppingCar car);
    void setMoney(String money);
    void setToPay(String pay);
    void toPayView(OrderDetail order);
}