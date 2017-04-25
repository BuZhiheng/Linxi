package cn.linxi.iu.com.presenter;
import android.content.Intent;
import android.widget.EditText;
import java.text.DecimalFormat;
import java.util.List;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.StationOilType;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessPreSaleOilPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IBusinessPreSaleOilView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/10/25.
 */
public class BusinessPreSaleOilPresenter implements IBusinessPreSaleOilPresenter {
    private IBusinessPreSaleOilView view;
    private Intent intent;
    private String sPrice;
    public BusinessPreSaleOilPresenter(IBusinessPreSaleOilView view,Intent intent){
        this.view = view;
        this.intent = intent;
    }
    @Override
    public void getOilType() {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        int operatId = PrefUtil.getInt(CommonCode.SP_USER_OPERA_ID, 0);
        String url = HttpUrl.businessPreSale+ OkHttpUtil.getSign()+"&operat_id="+operatId;
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
                    List<StationOilType> list = GsonUtil.jsonToList(result.data.result,StationOilType.class);
                    view.setOilType(list);
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }

    @Override
    public void checkPreSale(EditText etPurchase, StationOilType selecedPrice) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (selecedPrice == null){
            view.showToast("请选择油品");
            return;
        }
        String purchase = etPurchase.getText().toString();
        if (StringUtil.isNull(purchase)){
            view.showToast("请输入金额");
            return;
        }
        if (purchase.startsWith(".")||purchase.endsWith(".")){
            view.showToast("输入有误");
            return;
        }
        try {
            if (Float.parseFloat(purchase) == 0){
                view.showToast("输入有误");
                return;
            }
            if (Float.parseFloat(purchase) < 1){
                view.showToast("输入数值至少为1");
                return;
            }
        } catch (Exception e){
            view.showToast("输入有误");
            return;
        }
        String id = intent.getStringExtra(CommonCode.INTENT_COMMON);
        if (StringUtil.isNull(id)){
            view.showToast("手机号或车牌号为空");
            return;
        }
        if (id.length() > 8){
            view.showSureDialog("手机："+id,purchase);
        } else {
            view.showSureDialog("车牌："+id,purchase);
        }
    }
    @Override
    public void toPreSale(EditText etPurchase, StationOilType selecedPrice) {
        String id = intent.getStringExtra(CommonCode.INTENT_COMMON);
        int operatId = PrefUtil.getInt(CommonCode.SP_USER_OPERA_ID, 0);
        String purchase = etPurchase.getText().toString();
        RequestBody body = new FormBody.Builder()
                .add("operat_id", operatId + "")
                .add("identify", id)
                .add("amount", purchase)
                .add("details_id", selecedPrice.details_id)
                .build();
        OkHttpUtil.post(HttpUrl.businessPreOrder, body, new Subscriber<String>() {
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
                    view.saleSuccess();
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    @Override
    public void getPriceAndCount(StationOilType oil, EditText etMoney) {
        if (oil == null){
            view.setPrice("","");
            return;
        }
        if (StringUtil.isNull(oil.price)){
            view.setPrice("","");
            return;
        }
        if ("1".equals(oil.type)){
            sPrice = oil.oil_type+"/单价:"+oil.price+"元/升";
        } else {
            sPrice = oil.oil_type+"/单价:"+oil.price+"元/M³";
        }
        String sMoney = etMoney.getText().toString();
        if (StringUtil.isNull(sMoney)){
            view.setPrice(sPrice,"");
            return;
        }
        if (sMoney.startsWith(".")||sMoney.endsWith(".")){
            view.setPrice(sPrice,"");
            return;
        }
        float money = Float.parseFloat(sMoney);
        float price;
        String amount;
        try {
            price = Float.parseFloat(oil.price);
            float fCout = money/price;
            String iPrice = new DecimalFormat("0.00").format(fCout);
            if ("1".equals(oil.type)){
                sPrice = oil.oil_type+"/单价:"+oil.price+"元/升";
                amount = iPrice+" L";
            } else {
                sPrice = oil.oil_type+"/单价:"+oil.price+"元/M³";
                amount = iPrice+" M³";
            }
        } catch (Exception e){
            view.setPrice(sPrice,"");
            return;
        }
        view.setPrice(sPrice,amount);
    }
}