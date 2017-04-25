package cn.linxi.iu.com.view.iview;

import java.util.Map;

/**
 * Created by buzhiheng on 2016/8/22.
 */
public interface IRechargeView {
    void showToast(String toast);
    void rechargeSuccess();
    void aliPayResult(Map<String,String> result);
}
