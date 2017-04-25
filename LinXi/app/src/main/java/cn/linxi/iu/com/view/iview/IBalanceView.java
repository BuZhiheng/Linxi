package cn.linxi.iu.com.view.iview;
import cn.linxi.iu.com.model.UserCenterInfo;
/**
 * Created by buzhiheng on 2016/7/19.
 */
public interface IBalanceView {
    void showToast(String toast);
    void getInfoSuccess(UserCenterInfo info);
}