package cn.linxi.iu.com.presenter;
import android.content.Intent;
import android.widget.EditText;
import java.util.List;
import cn.linxi.iu.com.model.AutomacDetail;
import cn.linxi.iu.com.model.AutomacDetailFormat;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.OrderDetail;
import cn.linxi.iu.com.presenter.ipresenter.IAutomacDetailPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IAutomacDetailView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2017/3/13.
 */
public class AutomacDetailPresenter implements IAutomacDetailPresenter {
    private IAutomacDetailView view;
    private Intent intent;
    public AutomacDetailPresenter(IAutomacDetailView view,Intent intent){
        this.view = view;
        this.intent = intent;
    }
    @Override
    public void getAutomacDetail() {
        if (intent == null){
            return;
        }
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String gid = intent.getStringExtra(CommonCode.INTENT_STATION_GID);
        String uid = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        String url = HttpUrl.getStationAutomacTypeDataUrl + OkHttpUtil.getSign()+"&user_id="+uid+"&gid="+gid;
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
                    AutomacDetail detail = GsonUtil.jsonToObject(result.getResult(),AutomacDetail.class);
                    view.setAutomacDetail(detail);
                    List<AutomacDetailFormat> format = GsonUtil.jsonToList(detail.format,AutomacDetailFormat.class);
                    for (int i=0;i<format.size();i++){
                        view.setFormat(format.get(i));
                    }
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    @Override
    public void setCoutAdd(EditText editText) {
        String cout = editText.getText().toString();
        if (StringUtil.isNull(cout)){
            view.setCout("1");
            return;
        }
        Float fCout = Float.valueOf(cout)+1;
        view.setCout(fCout + "");
    }

    @Override
    public void setCoutSub(EditText editText) {
        String cout = editText.getText().toString();
        if (StringUtil.isNull(cout)){
            return;
        }
        Float fCout = Float.valueOf(cout);
        if (fCout <= 1){
            return;
        } else {
            fCout--;
            view.setCout(fCout+"");
        }
    }
    @Override
    public void addShoppingCar(EditText editText) {
        String cout = editText.getText().toString();
        if (StringUtil.isWrongNum(cout)){
            view.showToast("输入有误,请重新输入!");
            return;
        }
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String gid = intent.getStringExtra(CommonCode.INTENT_STATION_GID);
        String sid = intent.getStringExtra(CommonCode.INTENT_STATION_ID);
        String uid = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        String url = HttpUrl.addShoppingCarUrl;
        RequestBody body = new FormBody.Builder()
                .add("user_id", uid)
                .add("gid", gid)
                .add("stype", "1")
                .add("num", cout)
                .add("station_id", sid)
                .build();
        OkHttpUtil.post(url, body, new Subscriber<String>() {
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
                    view.showToast(result.error);
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    @Override
    public void order(EditText etCout) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String sCout = etCout.getText().toString();
        if (StringUtil.isWrongNum(sCout)){
            view.showToast("输入有误");
            return;
        }
        String gid = intent.getStringExtra(CommonCode.INTENT_STATION_GID);
        String sid = intent.getStringExtra(CommonCode.INTENT_STATION_ID);
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id", userId)
                .add("apply", "normal")
                .add("gid", gid)
                .add("num", sCout)
                .add("stype", "1")
                .add("station_id", sid)
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
}