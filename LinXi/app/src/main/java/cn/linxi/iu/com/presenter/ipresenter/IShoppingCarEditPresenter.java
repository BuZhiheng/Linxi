package cn.linxi.iu.com.presenter.ipresenter;
import android.widget.EditText;
import android.widget.ImageView;

import cn.linxi.iu.com.model.ShoppingCar;
import cn.linxi.iu.com.presenter.ShoppingCarEditPresenter;
/**
 * Created by buzhiheng on 2017/3/16.
 */
public interface IShoppingCarEditPresenter {
    void getShoppingCar();
    void deleteShoppingCar();
    void updateShoppingCar(ShoppingCarEditPresenter.OnUpdateListner listner);
    String addGoodsCout(EditText editText, ShoppingCar car);
    String subGoodsCout(EditText editText, ShoppingCar car);
    void onSelectClick(ShoppingCarEditPresenter.OnSelectClick onSelectClick, ImageView ivSelect, ShoppingCar car);
    void setShare();
}