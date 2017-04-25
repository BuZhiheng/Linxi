package cn.linxi.iu.com.presenter;
import android.Manifest;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import com.baidu.location.BDLocation;
import java.util.List;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.SelectCity;
import cn.linxi.iu.com.presenter.ipresenter.ISelectCityPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.MyLocationClient;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PermissionUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.ISelectCityView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/26.
 */
public class SelectCityPresenter implements ISelectCityPresenter {
    private ISelectCityView view;
    private AppCompatActivity context;
    public SelectCityPresenter(final ISelectCityView view,AppCompatActivity context){
        this.view = view;
        this.context = context;
        checkPermission();
    }
    @Override
    public void getCityList(int page) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String cityCode = PrefUtil.getString(CommonCode.SP_LOC_CITY_CODE,"0");
        String url = HttpUrl.getCityUrl+OkHttpUtil.getSign()+"&city_code="+cityCode+"&page="+page;
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
                if (result.success()) {
                    List<SelectCity> list = GsonUtil.jsonToList(result.data.result, SelectCity.class);
                    view.getCitySuccess(list);
                }
            }
        });
    }
    public void checkPermission() {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtil.checkNoPermission(context, permission)) {
                String[] reqPer = new String[]{Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
                context.requestPermissions(reqPer,100);
                if (PermissionUtil.checkDismissPermissionWindow(context,
                        permission)) {
                    view.showToast("位置获取失败,请开启定位权限重试");
                    return;
                }
                view.showToast("位置获取失败,请开启定位权限重试");
            } else {
                //App已经获取定位权限
                location();
            }
        } else {
            //Android系统小于 6.0
            location();
        }
    }
    private void location(){
        MyLocationClient.locSingle(new MyLocationClient.MyLocationListener() {
            @Override
            public void onLocSuccess(BDLocation bdLocation) {
                PrefUtil.putString(CommonCode.SP_LOC_CITY, bdLocation.getCity());
                PrefUtil.putFloat(CommonCode.SP_LOC_LAT, (float) bdLocation.getLatitude());
                PrefUtil.putFloat(CommonCode.SP_LOC_LNG, (float) bdLocation.getLongitude());
                PrefUtil.putString(CommonCode.SP_LOC_CITY_CODE, bdLocation.getCityCode());
                SelectCity city = new SelectCity();
                if (bdLocation.getCityCode() == null){
                    city.city_code = 0;
                } else {
                    city.city_code = Integer.parseInt(bdLocation.getCityCode());
                }
                city.city_name = bdLocation.getCity();
//                ToastUtil.show(bdLocation.getAddrStr()+bdLocation.getCityCode()+"select");
                view.setCurrentCity(city,bdLocation.getAddrStr());
            }
        });
    }
}