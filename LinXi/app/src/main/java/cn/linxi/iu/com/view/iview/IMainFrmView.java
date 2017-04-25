package cn.linxi.iu.com.view.iview;
import java.util.List;

import cn.linxi.iu.com.model.BuyFrmBanner;
import cn.linxi.iu.com.model.NormalPrice;
import cn.linxi.iu.com.model.SelectCity;
import cn.linxi.iu.com.model.Shared;
import cn.linxi.iu.com.model.Station;
/**
 * Created by buzhiheng on 2016/7/19.
 */
public interface IMainFrmView {
    void showToast(String toast);
    void removeStation();
    void addStationData(List<Station> list);
    void addPriceData(List<NormalPrice> list);
    void setPriceWidth(int width);
    void setBanner(List<BuyFrmBanner> banner);
    void setTimeOut();
    void haveNoStation(String err);
    void refreshBuy(SelectCity city);
    void setShare(Shared share);
    void toFirstLoginAct();
}