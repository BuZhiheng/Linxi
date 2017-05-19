package cn.linxi.iu.com.view.iview;
import java.util.List;
import cn.linxi.iu.com.model.Automac;
import cn.linxi.iu.com.model.UserHaveGoods;
/**
 * Created by buzhiheng on 2017/4/14.
 */
public interface IBusinessAfterScanView {
    void showToast(String toast);
    void setOilList(List<UserHaveGoods> list);
    void setGoodsList(List<UserHaveGoods> listGoods);
    void orderSuccess();
    void showUserBuyNothing();
    void showOrderSureDialog(List<Automac> listSure);
}