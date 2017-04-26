package cn.linxi.iu.com.presenter;
import android.support.v7.app.AppCompatActivity;
import java.util.List;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.Station;
import cn.linxi.iu.com.model.StationOilType;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessSaleOilPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.util.WindowUtil;
import cn.linxi.iu.com.view.iview.IBusinessSaleOilView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2017/4/26.
 */
public class BusinessSaleOilPresenter implements IBusinessSaleOilPresenter {
    private IBusinessSaleOilView view;
    public BusinessSaleOilPresenter(IBusinessSaleOilView view){
        this.view = view;
    }
    public void getStationDetail(final AppCompatActivity context) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String url = HttpUrl.getStationDetailUrl + OkHttpUtil.getSign() + "&user_id="+ 54 + "&station_id=5";
        OkHttpUtil.get(url, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
                BaseResult result = GsonUtil.jsonToObject(s, BaseResult.class);
                if (result.success()){
                    Station station = GsonUtil.jsonToObject(result.getResult(),Station.class);
                    List<StationOilType> priceList = GsonUtil.jsonToList(station.details, StationOilType.class);
                    view.setOilModel(priceList);
                    if (priceList.size() == 1){
                        view.setOilPriceWidth(WindowUtil.dp2px(context, 80));
                    } else if (priceList.size() == 2){
                        view.setOilPriceWidth(WindowUtil.dp2px(context, 240));
                    } else if (priceList.size() == 3){
                        view.setOilPriceWidth(WindowUtil.dp2px(context,360));
                    }
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
}