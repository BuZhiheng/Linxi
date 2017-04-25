package cn.linxi.iu.com.view.iview;
import cn.linxi.iu.com.model.TIModelCustomerOrderSure;
/**
 * Created by buzhiheng on 2016/9/7.
 */
public interface ITIMView {
    void timLoginSuccess();
    void timLoginError();
    void timOnForceOffline();
    void timOnUserSigExpired();
    void timOrderSure(TIModelCustomerOrderSure order);
    void showVoice();
    void showVib();
}