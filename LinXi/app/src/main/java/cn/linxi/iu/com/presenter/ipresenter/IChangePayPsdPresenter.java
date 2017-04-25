package cn.linxi.iu.com.presenter.ipresenter;
import android.widget.EditText;

/**
 * Created by buzhiheng on 2016/8/4.
 */
public interface IChangePayPsdPresenter {
    void change(EditText code,EditText psd,EditText psdConfirm);
    void getCode();
}