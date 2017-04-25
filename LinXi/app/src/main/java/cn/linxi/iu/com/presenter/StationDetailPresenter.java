package cn.linxi.iu.com.presenter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.baidu.location.BDLocation;

import java.util.List;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.Rebate;
import cn.linxi.iu.com.model.StationOilType;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.OrderDetail;
import cn.linxi.iu.com.model.Station;
import cn.linxi.iu.com.presenter.ipresenter.IStationDetailPresenter;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.MyLocationClient;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PermissionUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.util.WindowUtil;
import cn.linxi.iu.com.view.iview.IStationDetatilView;
import kr.co.namee.permissiongen.PermissionGen;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/19.
 */
public class StationDetailPresenter implements IStationDetailPresenter {
    private IStationDetatilView view;
    private String stationId;
    private Activity context;
    private Intent intent;
    public StationDetailPresenter(IStationDetatilView view){
        this.view = view;
        this.context = (Activity) view;
        this.intent = context.getIntent();
    }
    @Override
    public void getStationDetail() {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (intent == null){
            return;
        }
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        stationId = intent.getStringExtra(CommonCode.INTENT_STATION_ID);
        String url = HttpUrl.getStationDetailUrl + OkHttpUtil.getSign() + "&user_id="+ userId + "&station_id=" + stationId;
        OkHttpUtil.get(url, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
                BaseResult result = GsonUtil.jsonToObject(s,BaseResult.class);
                if (result.success()){
                    Station station = GsonUtil.jsonToObject(result.getResult(),Station.class);
                    List<StationOilType> priceList = GsonUtil.jsonToList(station.details, StationOilType.class);
                    List<Rebate> rebateList = GsonUtil.jsonToList(station.rebate_list,Rebate.class);
                    view.setStation(station,rebateList);
                    view.setOilModel(priceList);
                    if (priceList.size() == 1){
                        view.setOilPriceWidth(WindowUtil.dp2px((AppCompatActivity) view, 80));
                    } else if (priceList.size() == 2){
                        view.setOilPriceWidth(WindowUtil.dp2px((AppCompatActivity) view, 240));
                    } else if (priceList.size() == 3){
                        view.setOilPriceWidth(WindowUtil.dp2px((AppCompatActivity) view,360));
                    }
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }

    @Override
    public void addShoppingCar(StationOilType detail, EditText editText) {

    }

    @Override
    public void commitOrder(StationOilType detail, String purchase) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (detail == null){
            view.showToast("请选择油品");
            return;
        }
        String detailId = detail.details_id;
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        if (StringUtil.isNull(userId)){
            view.showToast("请重新登录");
            return;
        }
        if (StringUtil.isNull(stationId)){
            view.showToast("加油站获取失败");
            return;
        }
        if (StringUtil.isNull(purchase)){
            view.showToast("请输入油/气量");
            return;
        }
        if (purchase.startsWith(".")||purchase.endsWith(".")){
            view.showToast("输入油/气量有误");
            return;
        }
        try {
            if (Float.parseFloat(purchase) == 0){
                view.showToast("输入油/气量有误");
                return;
            }
            if (Float.parseFloat(purchase) < 1){
                view.showToast("输入数值至少为1");
                return;
            }
        } catch (Exception e){
            view.showToast("输入油/气量有误");
            return;
        }
        RequestBody body = new FormBody.Builder()
                .add("user_id",userId)
                .add("details_id", detailId)
                .add("purchase",purchase)
                .add("station_id",stationId)
                .build();
        OkHttpUtil.post(HttpUrl.addOrderUrl, body, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
                BaseResult result = GsonUtil.jsonToObject(s, BaseResult.class);
                if (result.success()) {
                    OrderDetail order = GsonUtil.jsonToObject(result.getResult(), OrderDetail.class);
                    view.commitOrderSuccess(order);
                    view.showToast(result.error);
                }
            }
        });
    }
    @Override
    public void oilCoutAdd(EditText etOil) {
        String cout = etOil.getText().toString();
        if (StringUtil.isNull(cout)){
            view.setOilCout("1");
            return;
        }
        Float fCout = Float.valueOf(cout)+1;
        view.setOilCout(fCout + "");
    }
    @Override
    public void oilCoutSub(EditText etOil) {
        String cout = etOil.getText().toString();
        if (StringUtil.isNull(cout)){
            return;
        }
        Float fCout = Float.valueOf(cout);
        if (fCout <= 1){
            return;
        } else {
            fCout--;
            view.setOilCout(fCout+"");
        }
    }
    @Override
    public void checkPermission(String permission,int code) {
        if (code != 100){
            LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if(!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // 未打开位置开关，可能导致定位失败或定位不准，提示用户或做相应处理
                view.showToast("未打开GPS");
                return;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtil.checkNoPermission(context, permission)) {
                if (PermissionUtil.checkDismissPermissionWindow(context,
                        permission)) {
                    if (code == 100){
                        view.showToast("请为本应用开启拨打电话权限");
                    } else {
                        view.showToast("请为本应用开启定位权限");
                    }
                    Intent intentSet = new Intent();
                    intentSet.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intentSet.setData(uri);
                    context.startActivity(intentSet);
                    return;
                }
                PermissionGen.with(context)
                        .addRequestCode(code)
                        .permissions(permission)
                        .request();
            } else {
                if (code == 100){
                    view.callPhone();
                } else {
                    toLocation();
                }
            }
        } else {
            if (code == 100){
                view.callPhone();
            } else {
                toLocation();
            }
        }
    }
    private void toLocation(){
        MyLocationClient.locSingle(new MyLocationClient.MyLocationListener() {
            @Override
            public void onLocSuccess(BDLocation bdLocation) {
                if (bdLocation == null){
                    view.showToast("定位失败");
                    return;
                }
                PrefUtil.putFloat(CommonCode.SP_LOC_LAT, (float) bdLocation.getLatitude());
                PrefUtil.putFloat(CommonCode.SP_LOC_LNG, (float) bdLocation.getLongitude());
                view.toNvg();
            }
        });
    }
}