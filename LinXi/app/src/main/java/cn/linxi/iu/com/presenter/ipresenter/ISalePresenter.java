package cn.linxi.iu.com.presenter.ipresenter;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by buzhiheng on 2016/8/25.
 */
public interface ISalePresenter {
    void getSaleCard(int page,SwipeRefreshLayout refresh);
}
