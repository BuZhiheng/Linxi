package cn.linxi.iu.com.presenter.ipresenter;
/**
 * Created by buzhiheng on 2016/8/22.
 */
public interface IOrderUnFinishPresenter {
    void getOrderList(int page);
    void removeOrder(String oid);
}