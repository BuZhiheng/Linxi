package cn.linxi.iu.com.presenter;
import android.widget.EditText;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.Shared;
import cn.linxi.iu.com.model.ShoppingCar;
import cn.linxi.iu.com.model.ShoppingCarListJson;
import cn.linxi.iu.com.presenter.ipresenter.IShoppingCarEditPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IShoppingCarEditView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2017/3/16.
 */
public class ShoppingCarEditPresenter implements IShoppingCarEditPresenter {
    private IShoppingCarEditView view;
    private List<ShoppingCar> listUpdate;
    private List<ShoppingCar> listDelete;
    public ShoppingCarEditPresenter(IShoppingCarEditView view){
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
                    view.removeView();
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
                    for (int i=0;i<list.size();i++){
                        view.addItem(list.get(i));
                    }
                    listUpdate = new ArrayList<>();
                    listDelete = list;
                }
            }
        });
    }
    @Override
    public void deleteShoppingCar() {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (listDelete == null || listDelete.size() == 0){
            view.showToast("请选择要删除的商品");
            return;
        }
        String json = StringUtil.getShoppingCarJson(listDelete);
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id",userId)
                .add("json", json)
                .build();
        OkHttpUtil.post(HttpUrl.removeShoppingCarUrl, body, new Subscriber<String>() {
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
                    getShoppingCar();
                }
                view.showToast(result.error);
            }
        });
    }
    @Override
    public void updateShoppingCar(final OnUpdateListner listner) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (listUpdate == null || listUpdate.size() == 0){
            if (listner != null){
                listner.success();
            }
            return;
        }
        String json = StringUtil.getShoppingCarJson(listUpdate);
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
                BaseResult result = GsonUtil.jsonToObject(s, BaseResult.class);
                if (result.success()) {
                    listner.success();
                }
                view.showToast(result.error);
            }
        });
    }
    @Override
    public String addGoodsCout(EditText editText, ShoppingCar car) {
        String str = editText.getText().toString();
        if (StringUtil.isNull(str)){
            return "1";
        }
        if (StringUtil.isWrongNum(str)){
            return "1";
        }
        float fStr = Float.parseFloat(str);
        fStr ++;
        updateList(fStr + "", car);
        return fStr+"";
    }
    @Override
    public String subGoodsCout(EditText editText, ShoppingCar car) {
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
        updateList(fStr+"",car);
        return fStr+"";
    }
    private void updateList(String s, ShoppingCar car){
        car.num = s;
        boolean isIn = false;
        for (int i=0;i<listUpdate.size();i++){
            ShoppingCar tempCar = listUpdate.get(i);
            if (tempCar.sid.equals(car.sid)){
                isIn = true;
                listUpdate.get(i).num = car.num;
            }
        }
        if (!isIn){
            listUpdate.add(car);
        }
    }
    @Override
    public void onSelectClick(OnSelectClick select, ImageView imageView, ShoppingCar car) {
        Object tag = imageView.getTag();
        if (tag != null){
            String st = (String) tag;
            if ("1".equals(st)){
                imageView.setTag("0");
                select.setUnSelect();
                listDelete.remove(car);
            } else if ("0".equals(st)){
                imageView.setTag("1");
                select.setSelect();
                listDelete.add(car);
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

    public interface OnSelectClick {
        void setSelect();
        void setUnSelect();
    }
    public interface OnUpdateListner{
        void success();
    }
}