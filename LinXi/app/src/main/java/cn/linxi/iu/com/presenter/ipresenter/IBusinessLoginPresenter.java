package cn.linxi.iu.com.presenter.ipresenter;
import android.app.Dialog;
import android.widget.EditText;

/**
 * Created by buzhiheng on 2016/8/4.
 */
public interface IBusinessLoginPresenter {
    void login(EditText username, EditText psd, Dialog dialog);
}