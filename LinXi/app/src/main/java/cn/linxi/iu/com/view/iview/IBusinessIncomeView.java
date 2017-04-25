package cn.linxi.iu.com.view.iview;
import java.util.List;
import cn.linxi.iu.com.model.OperateIncome;
import cn.linxi.iu.com.model.OperateIncomeItem;
/**
 * Created by buzhiheng on 2016/7/19.
 */
public interface IBusinessIncomeView {
    void showToast(String toast);
    void getInfoSuccess(OperateIncome income, List<OperateIncomeItem> list);
}