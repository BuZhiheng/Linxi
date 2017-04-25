package cn.linxi.iu.com.view.iview;
import java.util.List;
import java.util.Map;

import cn.linxi.iu.com.model.RechargeDiscount;
/**
 * Created by buzhiheng on 2017/3/8.
 */
public interface IRechargeDiscountView {
    void showToast(String toast);
    void setDiscount(List<RechargeDiscount> list);
    void aliPayResult(Map<String, String> stringStringMap);
}