package cn.linxi.iu.com.view.iview;
import java.util.List;
import cn.linxi.iu.com.model.TransferOrderDetail;
/**
 * Created by buzhiheng on 2017/5/17.
 */
public interface ITransferOrderDetailView {
    void showToast(String toast);
    void setData(List<TransferOrderDetail> list);
    void setNoData();
}