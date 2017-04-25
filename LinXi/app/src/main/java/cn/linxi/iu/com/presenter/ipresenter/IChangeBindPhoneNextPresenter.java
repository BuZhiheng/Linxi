package cn.linxi.iu.com.presenter.ipresenter;
import android.widget.EditText;
/**
 * Created by buzhiheng on 2016/8/4.
 */
public interface IChangeBindPhoneNextPresenter {
    void bind(EditText phone, EditText code);
    void getCode(EditText etPhone);
}