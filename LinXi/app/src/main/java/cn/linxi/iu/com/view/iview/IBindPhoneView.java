package cn.linxi.iu.com.view.iview;
/**
 * Created by buzhiheng on 2016/8/4.
 */
public interface IBindPhoneView {
    void showToast(String toast);
    void bindSuccess();
    void refreshCodeButton(String time);
    void setCodeBtnCanClick();
    void setCodeBtnCanNotClick();
    void setJumpBtn();
}