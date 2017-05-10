package cn.linxi.iu.com.view.iview;
import java.util.List;

import cn.linxi.iu.com.model.TransferSaleDetail;
import cn.linxi.iu.com.model.TransferSaleTagPrice;

/**
 * Created by buzhiheng on 2017/5/10.
 */
public interface ITransferSaleView {
    void showToast(String toast);
    void setData(TransferSaleDetail detail);

    void saleSuccess();

    void setTagPrice(List<TransferSaleTagPrice> prices);
}