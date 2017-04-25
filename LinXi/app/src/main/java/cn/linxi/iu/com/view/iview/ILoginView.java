package cn.linxi.iu.com.view.iview;
/**
 * Created by buzhiheng on 2016/8/4.
 */
public interface ILoginView {
    void showToast(String toast);
    void toMainActivity();
    void toBindPhoneActivity();
    void setTimeOut();
    void showQQDialog(String msg);
    void showWXDialog(String msg);
    void loginByQQ();
    void loginByWX();
    void setLastUsername(String username);
    void setLastLoginQQ();
    void setLastLoginWX();
    void setCodeBtnCanNotClick();
    void refreshCodeButton(String s);
    void setCodeBtnCanClick();
}