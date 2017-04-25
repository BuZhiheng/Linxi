package cn.linxi.iu.com.presenter;
import android.widget.EditText;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessPreSalePresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IBusinessPreSaleView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/10/26.
 */
public class BusinessPreSalePresenter implements IBusinessPreSalePresenter {
    private IBusinessPreSaleView view;
    public BusinessPreSalePresenter(IBusinessPreSaleView view){
        this.view = view;
    }
    @Override
    public void getCode(EditText etPhone) {
        /**
         * 绑定手机号获取验证码方法
         *
         * */
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String strPhone = etPhone.getText().toString();
        if (StringUtil.isNull(strPhone)){
            view.showToast("请输入手机号");
            return;
        }
        if (!StringUtil.strEX(strPhone, StringUtil.EX_PHONE)){
            view.showToast("请输入正确的手机号");
            return;
        }
        OkHttpUtil.get(HttpUrl.getCodeUrl + OkHttpUtil.getCodeSign(strPhone) + "&action=advance&mobile=" + strPhone, new Subscriber<String>() {
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
                    view.showToast("验证码发送成功");
                    view.setCodeBtnCanNotClick();
                    OkHttpUtil.executor.execute(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            for (int i = 60; i > 0; i--) {
                                                                view.refreshCodeButton(i + "");
                                                                Thread.sleep(1000);
                                                            }
                                                            view.setCodeBtnCanClick();
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                    );
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    @Override
    public void saleCheckCode(final EditText etPhone,EditText etCode) {
        /**
         * 验证验证码
         *
         * */
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        final String strPhone = etPhone.getText().toString();
        final String strCode = etCode.getText().toString();
        if (StringUtil.isNull(strPhone)){
            view.showToast("请输入手机号");
            return;
        }
        if (StringUtil.isNull(strCode)){
            view.showToast("请输入验证码");
            return;
        }
        if (!StringUtil.strEX(strPhone, StringUtil.EX_PHONE)){
            view.showToast("请输入正确的手机号");
            return;
        }
        if (strCode.length() != 6){
            view.showToast("请输入6位验证码");
            return;
        }
        RequestBody body = new FormBody.Builder()
                .add("mobile", strPhone)
                .add("action", "advance")
                .add("code", strCode)
                .build();
        OkHttpUtil.post(HttpUrl.verifyCodeUrl, body, new Subscriber<String>() {
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
                    view.toPreOilView(strPhone);
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    @Override
    public void sale(String province,EditText identify) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (StringUtil.isNull(province)){
            view.showToast("请选择省份");
            return;
        }
        String id = province+identify.getText().toString();
        if(StringUtil.isCarId(id)){
            view.toPreOilView(id);
        } else {
            view.showToast("请正确输入车牌号");
        }
    }
    @Override
    public void onCarCardPopClick(EditText plate, String s) {
        if (StringUtil.isNull(s)){
            return;
        }
        String p = plate.getText().toString();
        if (p.length() == 6){
            view.dismissCardPop();
            return;
        }
        p += s;
        view.setCardPlate(p);
        if (p.length() == 6){
            view.dismissCardPop();
        }
    }
    @Override
    public void onSubCarCardClick(EditText plate) {
        String p = plate.getText().toString();
        if (StringUtil.isNull(p)){
            return;
        }
        int length = p.length();
        p = p.substring(0,length-1);
        view.setCardPlate(p);
    }
}