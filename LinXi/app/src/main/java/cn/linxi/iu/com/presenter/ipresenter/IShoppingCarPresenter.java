package cn.linxi.iu.com.presenter.ipresenter;
import android.widget.EditText;
import android.widget.ImageView;

import cn.linxi.iu.com.model.ShoppingCar;
import cn.linxi.iu.com.presenter.ShoppingCarPresenter;
import cn.linxi.iu.com.view.fragment.ShoppingCarFragment;
/**
 * Created by buzhiheng on 2017/3/16.
 */
public interface IShoppingCarPresenter {
    void getShoppingCar();
    void onSelectClick(ShoppingCarPresenter.OnSelectClick select,ImageView imageView, ShoppingCar car);
    void deleteShoppingCar(ShoppingCarPresenter.OnDeleteSuccess success, ShoppingCar car);
    void payShoppingCar();
    String addGoodsCout(EditText editText);
    String subGoodsCout(EditText editText);
    void updateShoppingCar(ShoppingCarPresenter.OnUpdateSuccess onUpdateSuccess, ShoppingCar car, EditText editText);
}