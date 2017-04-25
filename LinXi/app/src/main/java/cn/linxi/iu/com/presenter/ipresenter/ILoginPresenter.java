package cn.linxi.iu.com.presenter.ipresenter;
import android.app.Dialog;
import android.widget.EditText;
/**
 * Created by buzhiheng on 2016/8/4.
 */
public interface ILoginPresenter {
    void login(String loginType, EditText username,EditText psd,Dialog dialog);
    void getCode(EditText username);
    void checkLoginType(String type);
}