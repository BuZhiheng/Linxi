package cn.linxi.iu.com.view.iview;

import java.util.List;

import cn.linxi.iu.com.model.StationOilType;

/**
 * Created by buzhiheng on 2017/4/26.
 */
public interface IBusinessSaleOilView {
    void showToast(String noticeNetworkDisconnect);

    void setOilPriceWidth(int i);

    void setOilModel(List<StationOilType> priceList);

}
