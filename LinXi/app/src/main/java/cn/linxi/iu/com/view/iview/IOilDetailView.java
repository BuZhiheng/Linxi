package cn.linxi.iu.com.view.iview;
import java.util.List;
import cn.linxi.iu.com.model.BalanceDetail;
/**
 * Created by buzhiheng on 2016/9/19.
 */
public interface IOilDetailView {
    void showToast(String toast);
    void getDetailSuccess(List<BalanceDetail> list);
}
