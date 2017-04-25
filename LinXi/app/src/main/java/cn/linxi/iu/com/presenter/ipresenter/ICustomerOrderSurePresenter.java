package cn.linxi.iu.com.presenter.ipresenter;
import android.content.Intent;
/**
 * Created by buzhiheng on 2016/9/9.
 */
public interface ICustomerOrderSurePresenter {
    void getOrder(Intent intent);
    void orderConfirm();
}