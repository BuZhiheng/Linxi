package cn.linxi.iu.com.view.iview;
import cn.linxi.iu.com.model.User;
/**
 * Created by buzhiheng on 2016/8/4.
 */
public interface IRegisterView {
    void showToast(String toast);
    void toNextActivity(User user);
    void refreshCodeButton(String time);
    void setCodeBtnCanClick();
    void setCodeBtnCanNotClick();
}