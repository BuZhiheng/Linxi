package cn.linxi.iu.com.view.iview;
import java.util.List;
import cn.linxi.iu.com.model.BossDetailMsg;
import cn.linxi.iu.com.model.BossDetailMsgEmployee;
/**
 * Created by buzhiheng on 2016/11/10.
 */
public interface IBossDetailView {
    void showToast(String toast);
    void getMsgEmployee(BossDetailMsg msg,List<BossDetailMsgEmployee> list);
}
