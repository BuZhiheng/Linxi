package cn.linxi.iu.com.view.iview;
import cn.linxi.iu.com.model.OperatUser;
/**
 * Created by buzhiheng on 2016/9/5.
 */
public interface IBusinessMainView {
    void showToast(String toast);
    void getUserSuccess(OperatUser user);
    void workOut();
    void cantWorkOut();
}