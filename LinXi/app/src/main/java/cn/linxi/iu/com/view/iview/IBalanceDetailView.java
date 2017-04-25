package cn.linxi.iu.com.view.iview;

import java.util.List;

import cn.linxi.iu.com.model.BalanceDetail;

/**
 * Created by buzhiheng on 2016/8/26.
 */
public interface IBalanceDetailView {
    void showToast(String toast);
    void getListSuccess(List<BalanceDetail> list);
}
