package cn.linxi.iu.com.presenter.ipresenter;
import cn.linxi.iu.com.model.RechargeDiscount;
/**
 * Created by buzhiheng on 2017/3/8.
 */
public interface IRechargeDiscountPresenter {
    void getDiscount();
    void onItemClick(RechargeDiscount discount);
    void pay(int payType);
}