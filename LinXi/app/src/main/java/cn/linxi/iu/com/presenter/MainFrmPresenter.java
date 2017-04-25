package cn.linxi.iu.com.presenter;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import com.baidu.location.BDLocation;
import java.util.List;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.BuyFrmBanner;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.NormalPrice;
import cn.linxi.iu.com.model.SelectCity;
import cn.linxi.iu.com.model.Shared;
import cn.linxi.iu.com.model.Station;
import cn.linxi.iu.com.presenter.ipresenter.IMainFrmPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.MyLocationClient;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PermissionUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.util.WindowUtil;
import cn.linxi.iu.com.view.iview.IMainFrmView;
import kr.co.namee.permissiongen.PermissionGen;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/7/19.
 */
public class MainFrmPresenter implements IMainFrmPresenter {
    private IMainFrmView view;
    private Activity context;
    public MainFrmPresenter(IMainFrmView view,Activity context){
        this.view = view;
        this.context = context;
        if (PrefUtil.getBoolean(CommonCode.SP_IS_NEW_USER,false)){
            view.toFirstLoginAct();
        }
    }
    @Override
    public void getOilList(final int page,SwipeRefreshLayout refresh,String cityCode) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        setTimeOut(refresh);
        String id = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        float lat = PrefUtil.getFloat(CommonCode.SP_LOC_LAT, 0);
        float lng = PrefUtil.getFloat(CommonCode.SP_LOC_LNG, 0);
        String currCode = PrefUtil.getString(CommonCode.SP_LOC_CITY_CODE,"");
        String url = HttpUrl.getStationListUrl + OkHttpUtil.getSign() + "&user_id="+id + "&page=" +page+"&city_code="+ cityCode+"&current_city_code="+currCode +"&longitude="+lng+"&latitude="+lat;
        if (StringUtil.isNull(currCode)){
            url = HttpUrl.getStationListUrl + OkHttpUtil.getSign() + "&user_id="+id + "&page=" +page+"&city_code="+ cityCode;
        }
        OkHttpUtil.get(url, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
//                view.showToast("error>getStationListUrl");
            }
            @Override
            public void onNext(String s) {
                BaseResult result = GsonUtil.jsonToObject(s,BaseResult.class);
                if (result.success()){
                    List<Station> stationList = GsonUtil.jsonToList(result.data.result,Station.class);
                    List<BuyFrmBanner> bannerList = GsonUtil.jsonToList(result.data.banner,BuyFrmBanner.class);
                    view.setBanner(bannerList);
                    if (page == 1 && stationList.size() == 0){
                        view.haveNoStation("当前城市没有开通哦~");
                        view.removeStation();
                    } else {
                        view.addStationData(stationList);
                    }
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    @Override
    public void getTodayPrice(String cityCode) {
        String id = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        String url = HttpUrl.getTodayPriceUrl + OkHttpUtil.getSign() + "&user_id="+id +"&city_code="+cityCode;
        OkHttpUtil.get(url, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
//                view.showToast("error>getTodayPriceUrl");
            }
            @Override
            public void onNext(String s) {
//                ToastUtil.show(s);
                BaseResult result = GsonUtil.jsonToObject(s,BaseResult.class);
                if (result.success()){
                    List<NormalPrice> priceList = GsonUtil.jsonToList(result.data.result,NormalPrice.class);
                    view.addPriceData(priceList);
                    if (priceList.size() == 1){
                        view.setPriceWidth(WindowUtil.dp2px((AppCompatActivity) view, 80));
                    } else if (priceList.size() == 2){
                        view.setPriceWidth(WindowUtil.dp2px((AppCompatActivity) view, 240));
                    } else if (priceList.size() == 3){
                        view.setPriceWidth(WindowUtil.dp2px((AppCompatActivity) view, 360));
                    }
                }
            }
        });
    }
    public void setTimeOut(final SwipeRefreshLayout refresh) {
        OkHttpUtil.executor.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(CommonCode.HTTP_TIMEOUT);
                            if (refresh.isRefreshing()) {
                                view.setTimeOut();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
    @Override
    public void initBuy() {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        SelectCity city = new SelectCity();
        String locCity = PrefUtil.getString(CommonCode.SP_LOC_CITY, "");
        String locCode = PrefUtil.getString(CommonCode.SP_LOC_CITY_CODE,"");

        String lastCity = PrefUtil.getString(CommonCode.SP_LOC_CITY_LAST,"");
        String lastCode = PrefUtil.getString(CommonCode.SP_LOC_CITY_CODE_LAST, "");
        if (!StringUtil.isNull(lastCode)){
            city.city_name = lastCity;
            city.city_code = Integer.parseInt(lastCode);
            view.refreshBuy(city);
            return;
        } else {
            if (!StringUtil.isNull(locCode)){
                city.city_name = locCity;
                city.city_code = Integer.parseInt(locCode);
                view.refreshBuy(city);
                return;
            } else {
                LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                if(!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // 未打开位置开关，可能导致定位失败或定位不准，提示用户或做相应处理
                    view.showToast("未打开GPS,已设置默认城市");
                    city.city_code = 174;
                    city.city_name = "东营市";
                    view.refreshBuy(city);
                    return;
                }
                String permission = Manifest.permission.ACCESS_FINE_LOCATION;
                String[] reqPer = new String[]{Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (PermissionUtil.checkNoPermission(context, permission)) {
                        if (PermissionUtil.checkDismissPermissionWindow(context,
                                permission)) {
                            city.city_code = 174;
                            city.city_name = "东营市";
                            view.refreshBuy(city);
                            view.showToast("请去设置为本应用打开位置权限,并重新进入App;或者点击左上角位置选择城市");
                            return;
                        }
                        PermissionGen.with(context)
                                .addRequestCode(100)
                                .permissions(reqPer)
                                .request();
                    } else {
                        location();
                    }
                } else {
                    location();
                }
            }
        }
    }
    @Override
    public void setShare() {
        Shared share = new Shared();
        String title = PrefUtil.getString(CommonCode.SP_SHARE_TITLE,"");
        String desc = PrefUtil.getString(CommonCode.SP_SHARE_DESC,"");
        String url = PrefUtil.getString(CommonCode.SP_SHARE_URL,"");
        if (StringUtil.isNull(url)){
            view.showToast("分享信息有误");
            return;
        }
        share.setTitle(title);
        share.setDesc(desc);
        share.setUrl(url);
        share.setImgUrl(CommonCode.APP_ICON);
        view.setShare(share);
    }
    public void location(){
        MyLocationClient.locSingle(new MyLocationClient.MyLocationListener() {
            @Override
            public void onLocSuccess(BDLocation loc) {
                PrefUtil.putString(CommonCode.SP_LOC_CITY, loc.getCity());
                PrefUtil.putFloat(CommonCode.SP_LOC_LAT, (float) loc.getLatitude());
                PrefUtil.putFloat(CommonCode.SP_LOC_LNG, (float) loc.getLongitude());
                PrefUtil.putString(CommonCode.SP_LOC_CITY_CODE, loc.getCityCode());
                SelectCity city = new SelectCity();
                city.city_name = loc.getCity();
                    if (!StringUtil.isNull(loc.getCityCode())) {
                    city.city_code = Integer.parseInt(loc.getCityCode());
                } else {
                    city.city_code = 0;
                }
                view.refreshBuy(city);
            }
        });
    }
}