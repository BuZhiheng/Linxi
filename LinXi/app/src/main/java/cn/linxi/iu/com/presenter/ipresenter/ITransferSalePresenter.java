package cn.linxi.iu.com.presenter.ipresenter;
import android.content.Intent;
import android.widget.EditText;
/**
 * Created by buzhiheng on 2017/5/10.
 */
public interface ITransferSalePresenter {
    void getData();
    String onPriceSub(EditText editText);
    String onPriceAdd(EditText editText);
    String onCoutSub(EditText editText);
    String onCoutAdd(EditText editText);
    void sure(EditText price, EditText cout);
}