package cn.linxi.iu.com.view.iview;
import cn.linxi.iu.com.model.UpdateMsg;
/**
 * Created by buzhiheng on 2016/9/22.
 */
public interface IUpdateView {
    void showToast(String toast);
    void showUpdate(UpdateMsg msg);
}