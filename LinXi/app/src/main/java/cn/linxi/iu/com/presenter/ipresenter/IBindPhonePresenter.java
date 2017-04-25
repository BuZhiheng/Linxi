package cn.linxi.iu.com.presenter.ipresenter;
import android.content.Intent;
import android.widget.EditText;
/**
 * Created by buzhiheng on 2016/8/4.
 */
public interface IBindPhonePresenter {
    void bind(EditText username, EditText code);
    void getCode(EditText phone);
    void showJump(Intent intent);
}