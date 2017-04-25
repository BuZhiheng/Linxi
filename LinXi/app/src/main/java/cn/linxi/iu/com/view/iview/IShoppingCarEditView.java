package cn.linxi.iu.com.view.iview;
import cn.linxi.iu.com.model.Shared;
import cn.linxi.iu.com.model.ShoppingCar;
/**
 * Created by buzhiheng on 2017/3/16.
 */
public interface IShoppingCarEditView {
    void showToast(String toast);
    void addItem(ShoppingCar car);
    void removeView();
    void setShare(Shared share);
}