package cn.linxi.iu.com.presenter.ipresenter;
import android.widget.EditText;
/**
 * Created by buzhiheng on 2017/5/10.
 */
public interface ITransferBuyPresenter {
    void getData();
    String onCoutSub(EditText editText);
    String onCoutAdd(EditText editText);
    void sure(EditText price, EditText cout);
}