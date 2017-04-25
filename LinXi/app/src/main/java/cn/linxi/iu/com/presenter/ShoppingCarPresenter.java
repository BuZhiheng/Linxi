package cn.linxi.iu.com.presenter;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.OrderDetail;
import cn.linxi.iu.com.model.ShoppingCar;
import cn.linxi.iu.com.model.ShoppingCarListJson;
import cn.linxi.iu.com.presenter.ipresenter.IShoppingCarPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.fragment.ShoppingCarFragment;
import cn.linxi.iu.com.view.iview.IShoppingCarView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2017/3/16.
 */
public class ShoppingCarPresenter implements IShoppingCarPresenter {
    private IShoppingCarView view;
    private List<ShoppingCar> listCar;
    public ShoppingCarPresenter(IShoppingCarView view){
        this.view = view;
    }
    @Override
    public void getShoppingCar() {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String url = HttpUrl.getShoppingCarUrl + OkHttpUtil.getSign() + "&user_id=" + PrefUtil.getInt(CommonCode.SP_USER_USERID, 0);
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
                    ShoppingCarListJson info = GsonUtil.jsonToObject(result.getResult(), ShoppingCarListJson.class);
                    List<ShoppingCar> list = GsonUtil.jsonToList(info.oil,ShoppingCar.class);
                    if (list.size() > 0){
                        list.get(0).orderType = 1;
                    }
                    List<ShoppingCar> listAuto = GsonUtil.jsonToList(info.goods, ShoppingCar.class);
                    if (listAuto.size() > 0){
                        listAuto.get(0).orderType = 2;
                    }
                    list.addAll(listAuto);
                    listCar = list;
                    for (int i=0;i<list.size();i++){
                        view.addItem(list.get(i));
                    }
                    setMoney();
                    view.setToPay("结算(" + listCar.size() + ")");
                }
            }
        });
    }
    @Override
    public void onSelectClick(OnSelectClick select, ImageView imageView, ShoppingCar car) {
        Object tag = imageView.getTag();
        if (tag != null){
            String st = (String) tag;
            if ("1".equals(st)){
                imageView.setTag("0");
                select.setUnSelect();
                listCar.remove(car);
            } else if ("0".equals(st)){
                imageView.setTag("1");
                select.setSelect();
                listCar.add(car);
            }
            view.setToPay("结算("+listCar.size()+")");
            setMoney();
        }
    }
    private void setMoney(){
        if (listCar != null){
            float money = 0;
            for (int i=0;i<listCar.size();i++){
                ShoppingCar car = listCar.get(i);
                if (!StringUtil.isWrongNum(car.now_price)){
                    float p = Float.parseFloat(car.now_price);
                    float c = Float.parseFloat(car.num);
                    money += p*c;
                }
            }
            view.setMoney("¥ "+money);
        }
    }
    @Override
    public void deleteShoppingCar(final OnDeleteSuccess listener, final ShoppingCar car) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id",userId)
                .add("sid", car.sid)
                .build();
        OkHttpUtil.post(HttpUrl.deleteShoppingCarUrl, body, new Subscriber<String>() {
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
                    listener.success();
                }
                view.showToast(result.error);
            }
        });
    }
    @Override
    public void payShoppingCar() {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (listCar == null || listCar.size() == 0){
            view.showToast("购物车为空,请先去购买商品");
            return;
        }
        String json = StringUtil.getShoppingCarJson(listCar);
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id",userId)
                .add("order_json", json)
                .add("apply","cart")
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
                BaseResult result = GsonUtil.jsonToObject(s,BaseResult.class);
                if (result.success()){
                    //去结算页面
                    OrderDetail order = GsonUtil.jsonToObject(result.getResult(),OrderDetail.class);
                    view.toPayView(order);
                }
                view.showToast(result.error);
            }
        });
    }
    @Override
    public String addGoodsCout(EditText editText) {
        String str = editText.getText().toString();
        if (StringUtil.isNull(str)){
            return "1";
        }
        if (StringUtil.isWrongNum(str)){
            return "1";
        }
        float fStr = Float.parseFloat(str);
        fStr ++;
        return fStr+"";
    }
    @Override
    public String subGoodsCout(EditText editText) {
        String str = editText.getText().toString();
        if (StringUtil.isNull(str)){
            return "1";
        }
        if (StringUtil.isWrongNum(str)){
            return "1";
        }
        float fStr = Float.parseFloat(str);
        if (fStr > 1){
            fStr --;
        }
        return fStr+"";
    }
    @Override
    public void updateShoppingCar(final OnUpdateSuccess listener, final ShoppingCar car, EditText editText) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String s = editText.getText().toString();
        if (StringUtil.isWrongNum(s)){
            view.showToast("输入有误");
            return;
        }
        car.num = s;
        String json = "";
        if (car != null){
            json = "["+StringUtil.getShoppingCarJs(car)+"]";
        }
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id",userId)
                .add("json", json)
                .build();
        OkHttpUtil.post(HttpUrl.updateShoppingCarUrl, body, new Subscriber<String>() {
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
                    listener.success(car);
                    if (listCar.size() > 0){
                        for (int i=0;i<listCar.size();i++){
                            if (listCar.get(i).sid.equals(car.sid)){
                                listCar.get(i).num = car.num;
                            }
                        }
                        setMoney();
                    }
                }
                view.showToast(result.error);
            }
        });
    }
    public interface OnUpdateSuccess {
        void success(ShoppingCar car);
    }
    public interface OnDeleteSuccess {
        void success();
    }
    public interface OnSelectClick {
        void setSelect();
        void setUnSelect();
    }
}