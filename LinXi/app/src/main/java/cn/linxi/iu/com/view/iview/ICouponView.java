package cn.linxi.iu.com.view.iview;

import java.util.List;

import cn.linxi.iu.com.model.CouponCard;

/**
 * Created by buzhiheng on 2016/8/29.
 */
public interface ICouponView {
    void showToast(String toast);
    void getListSuccess(List<CouponCard> list);
}
