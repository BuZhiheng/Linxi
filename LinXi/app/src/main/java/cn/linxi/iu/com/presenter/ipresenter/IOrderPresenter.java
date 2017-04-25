package cn.linxi.iu.com.presenter.ipresenter;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.linxi.iu.com.model.OrderOil;
import cn.linxi.iu.com.presenter.OrderPresenter;
/**
 * Created by buzhiheng on 2017/4/17.
 */
public interface IOrderPresenter {
    void getOrderDetail(Intent intent);
    void payOrder(LinearLayout llOilItem, ImageView ivCheckBalance);
    void payByPsd(String psd);
    void onBalanceClick(ImageView ivCheckBalance);
    void onAliClick();
    void onWxClick();
    void updateOrder(OrderOil sid, ImageView iv,OrderPresenter.UpdateOrderListener listener);
}