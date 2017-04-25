package cn.linxi.iu.com.view.iview;
import cn.linxi.iu.com.model.SaleOilCardMsg;
/**
 * Created by buzhiheng on 2016/9/22.
 */
public interface IMyOilCardDetailView {
    void showToast(String toast);
    void setContent(SaleOilCardMsg card);
    void showPayPsdDialog();
    void showNotBindPayPsd();
    void showNotBindPhone();
    void saleSuccess();
}