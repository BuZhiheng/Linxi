package cn.linxi.iu.com.presenter.ipresenter;
import android.support.v4.widget.SwipeRefreshLayout;
/**
 * Created by buzhiheng on 2016/7/19.
 */
public interface IMainFrmPresenter {
    void getOilList(int page,SwipeRefreshLayout refresh,String cityCode);
    void getTodayPrice(String cityCode);
    void initBuy();
    void setShare();
}