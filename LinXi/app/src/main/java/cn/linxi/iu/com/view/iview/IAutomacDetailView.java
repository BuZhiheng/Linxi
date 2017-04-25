package cn.linxi.iu.com.view.iview;
import cn.linxi.iu.com.model.AutomacDetail;
import cn.linxi.iu.com.model.AutomacDetailFormat;
import cn.linxi.iu.com.model.OrderDetail;
/**
 * Created by buzhiheng on 2017/3/13.
 */
public interface IAutomacDetailView {
    void showToast(String toast);
    void setAutomacDetail(AutomacDetail automac);
    void setCout(String cout);
    void setFormat(AutomacDetailFormat format);
    void toPayView(OrderDetail order);
}