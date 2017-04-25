package cn.linxi.iu.com.view.iview;
import java.util.List;
import cn.linxi.iu.com.model.BossMsgEmployeeDetail;
import cn.linxi.iu.com.model.BossTodayAmount;
import cn.linxi.iu.com.model.BossTodayPurchase;
/**
 * Created by buzhiheng on 2016/11/9.
 */
public interface IBossView {
    void showToast(String toast);
    void setSalendar(String salendar);
    void getProfitSuccess(String profit);
    void getTodayMsgAmountSuccess(BossTodayAmount amount);
    void getTodayMsgPurchaseSuccess(BossTodayPurchase purchase);
    void getMsgEmployee(List<BossMsgEmployeeDetail> list);
}