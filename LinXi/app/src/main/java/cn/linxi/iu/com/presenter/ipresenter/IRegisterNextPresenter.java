package cn.linxi.iu.com.presenter.ipresenter;
import android.content.Intent;
import android.widget.EditText;
/**
 * Created by buzhiheng on 2016/8/4.
 */
public interface IRegisterNextPresenter {
    void register(Intent user, EditText psd, EditText psdConfirm,EditText etInvite);
}