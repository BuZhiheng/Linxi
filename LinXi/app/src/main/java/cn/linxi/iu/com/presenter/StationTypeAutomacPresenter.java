package cn.linxi.iu.com.presenter;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.Automac;
import cn.linxi.iu.com.model.AutomacBanner;
import cn.linxi.iu.com.model.AutomacListJson;
import cn.linxi.iu.com.model.AutomacType;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IStationOilypeAutomacFrmPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.util.WindowUtil;
import cn.linxi.iu.com.view.iview.IStationOilTypeAutomacFrmView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2017/3/6.
 */
public class StationTypeAutomacPresenter implements IStationOilypeAutomacFrmPresenter {
    private IStationOilTypeAutomacFrmView view;
    private Intent intent;
    private String curr_sort_sale = CommonCode.ORDER_BY_DESC;
    private String curr_sort_price = CommonCode.ORDER_BY_ASC;
    private String pid = "";
    private boolean isByType = false;
    public StationTypeAutomacPresenter(IStationOilTypeAutomacFrmView view, Intent intent){
        this.view = view;
        this.intent = intent;
    }
    @Override
    public void getAutoMacData() {
        int userId = PrefUtil.getInt(CommonCode.SP_USER_USERID, 0);
        String cid = intent.getStringExtra(CommonCode.INTENT_STATION_CID);
        String sid = intent.getStringExtra(CommonCode.INTENT_STATION_ID);
        String url = HttpUrl.getStationAutomacTypeListUrl+ OkHttpUtil.getSign()+"&user_id="+userId+"&cid="+cid+"&pid="+pid+"&station_id="+sid+"&saled="+curr_sort_sale+"&price="+curr_sort_price;
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
                    AutomacListJson json = GsonUtil.jsonToObject(result.getResult(), AutomacListJson.class);
                    if (json == null){
                        return;
                    }
                    if (json.product != null){
                        List<Automac> auto = GsonUtil.jsonToList(json.product,Automac.class);
                        view.setAutomacData(auto);
                    }
                    if (!isByType){
                        if (json.banner != null){
                            List<AutomacBanner> banner = GsonUtil.jsonToList(json.banner,AutomacBanner.class);
                            view.setAutomacBanner(banner);
                        }
                        if (json.menu != null){
                            List<AutomacType> type = GsonUtil.jsonToList(json.menu,AutomacType.class);
                            view.setAutomacType(type);
                            if (type.size() == 1){
                                view.setOilPriceWidth(WindowUtil.dp2px((AppCompatActivity) ((Fragment) view).getActivity(), 100));
                            } else if (type.size() == 2){
                                view.setOilPriceWidth(WindowUtil.dp2px((AppCompatActivity) ((Fragment) view).getActivity(), 200));
                            } else if (type.size() == 3){
                                view.setOilPriceWidth(WindowUtil.dp2px((AppCompatActivity) ((Fragment) view).getActivity(),300));
                            }
                        }
                    }
                }
            }
        });
    }
    @Override
    public void getAutoDataBySale() {//点击销量
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (curr_sort_sale.equals(CommonCode.ORDER_BY_DESC)){
            curr_sort_sale = CommonCode.ORDER_BY_ASC;
            view.setSortSale(R.drawable.ic_oiltype_automac_desc);
        } else {
            curr_sort_sale = CommonCode.ORDER_BY_DESC;
            view.setSortSale(R.drawable.ic_oiltype_automac_asc);
        }
        getAutoMacData();
    }
    @Override
    public void getAutoDataByPrice() {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (curr_sort_price.equals(CommonCode.ORDER_BY_DESC)){
            curr_sort_price = CommonCode.ORDER_BY_ASC;
            view.setSortPrice(R.drawable.ic_oiltype_automac_desc);
        } else {
            curr_sort_price = CommonCode.ORDER_BY_DESC;
            view.setSortPrice(R.drawable.ic_oiltype_automac_asc);
        }
        getAutoMacData();
    }
    @Override
    public void getAutoDataByPid(String pid) {
        this.pid = pid;
        isByType = true;
        getAutoMacData();
    }
}