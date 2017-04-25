package cn.linxi.iu.com.presenter.ipresenter;
import android.widget.EditText;
/**
 * Created by buzhiheng on 2016/8/4.
 */
public interface IForgetPresenter {
    void forget(EditText username, EditText code);
    void getCode(EditText username);
}