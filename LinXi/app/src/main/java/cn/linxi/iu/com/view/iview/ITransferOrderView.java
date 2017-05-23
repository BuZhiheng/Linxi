package cn.linxi.iu.com.view.iview;
import cn.linxi.iu.com.model.TransferOrder;
import cn.linxi.iu.com.model.TransferOrderDetail;
/**
 * Created by buzhiheng on 2017/5/22.
 */
public interface ITransferOrderView {
    void showToast(String toast);
    void setOrderData(TransferOrder order);
    void setOrderItem(TransferOrderDetail detail);
}