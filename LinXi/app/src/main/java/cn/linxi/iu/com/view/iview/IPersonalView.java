package cn.linxi.iu.com.view.iview;
/**
 * Created by buzhiheng on 2016/9/7.
 */
public interface IPersonalView {
    void showToast(String toast);
    void showProdialog();
    void setVip(int vipIc,String vipDsc);
    void setProDialog(String msg);
    void uploadSuccess(String url);
    void setInviteMobile(String phone);
    void camera();
}