package cn.linxi.iu.com.view.iview;

import java.util.List;

import cn.linxi.iu.com.model.Sign;
import cn.linxi.iu.com.model.SignReward;

/**
 * Created by buzhiheng on 2016/8/29.
 */
public interface ISignView {
    void showToast(String toast);
    void setSignView(List<Sign> data);
    void signSuccess(SignReward reward);
}
