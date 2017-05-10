package cn.linxi.iu.com.view.iview;

import java.util.List;

import cn.linxi.iu.com.model.SaleOilCard;

/**
 * Created by buzhiheng on 2017/5/4.
 */
public interface ITransferMarketView {
    void showToast(String toast);

    void setTransferMarket(List<SaleOilCard> list);
}