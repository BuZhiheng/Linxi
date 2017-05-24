package cn.linxi.iu.com.presenter;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.Automac;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.BusinessAfterScanJson;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.EventSaleSuccess;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.UserHaveGoods;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessAfterScanPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IBusinessAfterScanView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2017/4/14.
 */
public class BusinessAfterScanPresenter implements IBusinessAfterScanPresenter {
    private IBusinessAfterScanView view;
    private String code;
    private BusinessAfterScanJson json;
    private List<UserHaveGoods> listGoods;
    private UserHaveGoods goods;
    private JSONArray array;
    public BusinessAfterScanPresenter(IBusinessAfterScanView view){
        this.view = view;
    }
    @Override
    public void getData(Intent intent) {
        if (intent == null){
            return;
        }
        if (!SystemUtils.networkState()){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        code = intent.getStringExtra(CommonCode.INTENT_QRCODE);
//        String stationId = PrefUtil.getString(CommonCode.SP_USER_STATION_ID, "");
        String operatId = PrefUtil.getInt(CommonCode.SP_USER_OPERA_ID,0)+"";
        RequestBody body = new FormBody.Builder()
//                .add("token",token)
//                .add("station_id",stationId)
                .add("operat_id", operatId)
                .build();
        OkHttpUtil.post(HttpUrl.finalUrl + code, body, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
                view.showToast(s);
                BaseResult result = GsonUtil.jsonToObject(s, BaseResult.class);
                if (result.success()) {
                    json = GsonUtil.jsonToObject(result.getResult(),BusinessAfterScanJson.class);
                    boolean oilIsNull = false;
                    if (json.oil != null){
                        List<UserHaveGoods> list = GsonUtil.jsonToList(json.oil,UserHaveGoods.class);
                        if (list.size() > 0){
                            view.setOilList(list);
                        } else {
                            oilIsNull = true;
                        }
                    } else {
                        oilIsNull = true;
                    }
                    if (json.goods != null){
                        listGoods = GsonUtil.jsonToList(json.goods,UserHaveGoods.class);
                        if (listGoods.size() > 0){
                            view.setGoodsList(listGoods);
                        } else {
                            if (oilIsNull){
                                view.showUserBuyNothing();
                            }
                        }
                    } else {
                        if (oilIsNull){
                            view.showUserBuyNothing();
                        }
                    }
                } else {
                    view.showToast(result.error);
                    view.orderSuccess();
                }
            }
        });
    }
    @Override
    public void orderSure(EditText etOilPurchase, LinearLayout llGoods, LinearLayout llGoodsCout) {
        List<Automac> listSure = new ArrayList<>();
        array = new JSONArray();
        if (listGoods == null || listGoods.size() == 0){
            if (goods == null){
                view.showToast("未选择物品");
                return;
            }
        } else {
            for (int i=0;i<llGoods.getChildCount();i++){
                View vGoods = llGoods.getChildAt(i);
                ImageView ivCheck = (ImageView) vGoods.findViewById(R.id.iv_business_afterscan_check);
                if ("1".equals(ivCheck.getTag())){
                    View vCout = llGoodsCout.getChildAt(i);
                    EditText etCout = (EditText) vCout.findViewById(R.id.et_business_afterscan_auto);
                    UserHaveGoods automac = listGoods.get(i);
                    String sCout = etCout.getText().toString();
                    if (StringUtil.isWrongNum(sCout)){
                        view.showToast("请正确输入"+automac.name+"数量");
                        return;
                    }
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("gid",automac.gid);
                        jsonObject.put("num",sCout);
                        jsonObject.put("type",1);
                        array.put(jsonObject);
                        Automac a = new Automac();
                        a.name = automac.name;
                        a.num = sCout;
                        listSure.add(a);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (goods != null){
            String oilPurchase = etOilPurchase.getText().toString();
            if (StringUtil.isWrongNum(oilPurchase)){
                view.showToast("请正确输入油量");
                return;
            }
            JSONObject jsonObjectOil = new JSONObject();
            try {
                jsonObjectOil.put("gid",goods.gid);
                jsonObjectOil.put("num",oilPurchase);
                jsonObjectOil.put("type",0);
                array.put(jsonObjectOil);
                Automac a = new Automac();
                a.name = goods.name;
                a.num = oilPurchase;
                listSure.add(a);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (listSure.size() == 0){
            view.showToast("请选择消费商品");
            return;
        }
        view.showOrderSureDialog(listSure);
    }

    @Override
    public void setOilCheck(ImageView imageView, UserHaveGoods mac, OnGoodsCheckListener onGoodsCheckListener) {
        String tag = (String) imageView.getTag();
        if ("1".equals(tag)){
            onGoodsCheckListener.onClick(R.drawable.ic_station_check,"0");
            this.goods = null;
        } else {
            onGoodsCheckListener.onClick(R.drawable.ic_station_checked,"1");
            this.goods = mac;
        }
    }

    public void order(){
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (array == null){
            view.showToast("数据有误");
            return;
        }
        String stationId = PrefUtil.getString(CommonCode.SP_USER_STATION_ID, "");
        String operaId = PrefUtil.getInt(CommonCode.SP_USER_OPERA_ID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id", json.user_id)
                .add("operat_id", operaId)
                .add("station_id", stationId)
                .add("type", "1")
                .add("json",array.toString())
                .build();
//        view.showToast(json.user_id+"\n"+operaId+"\n"+stationId+"\n"+array.toString());
        String url = HttpUrl.businessOrderCreate;
        if (!StringUtil.isNull(code) && code.contains("source=weixin")){
            url = HttpUrl.businessOrderCreateWX;
        }
        OkHttpUtil.post(url, body, new Subscriber<String>() {
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
                    EventBus.getDefault().post(new EventSaleSuccess());
                    view.showToast("创建成功");
                    view.orderSuccess();
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    @Override
    public void setGoodsCheck(ImageView imageView, OnGoodsCheckListener onGoodsCheckListener) {
        String tag = (String) imageView.getTag();
        if ("1".equals(tag)){
            onGoodsCheckListener.onClick(R.drawable.ic_station_check,"0");
        } else {
            onGoodsCheckListener.onClick(R.drawable.ic_station_checked,"1");
        }
    }
    public interface OnGoodsCheckListener{
        void onClick(int drawable, String tag);
    }
}