package cn.linxi.iu.com.presenter.ipresenter;
import android.widget.EditText;

import cn.linxi.iu.com.model.StationOilType;

/**
 * Created by buzhiheng on 2017/5/10.
 */
public interface ITransferBuyPresenter {
    void getData();
    String onCoutSub(EditText editText, StationOilType selecedPrice);
    String onCoutAdd(EditText editText, StationOilType selecedPrice);
    void sure(EditText price, EditText cout);
    void getTransferMoney(EditText editText, StationOilType selecedPrice);

    void order(EditText editText, StationOilType selecedPrice);

}