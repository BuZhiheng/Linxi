package cn.linxi.iu.com.presenter.ipresenter;
import android.content.Intent;
/**
 * Created by buzhiheng on 2016/8/22.
 */
public interface IOrderDetailPresenter {
    void getOrderDetail(Intent intent);
    void payOrder();
    void payByPsd(String psd);
    void onBalanceClick();
    void onAliClick();
    void onWxClick();
    void cancelOrder();
}
