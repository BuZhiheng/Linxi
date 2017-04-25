package cn.linxi.iu.com.wxapi;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;

import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.EventWxPaySuccess;
import cn.linxi.iu.com.util.ToastUtil;
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
    private IWXAPI api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, CommonCode.APP_ID_WX);
        api.handleIntent(getIntent(), this);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }
    @Override
    public void onReq(BaseReq req) {
    }
    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode){
                case 0:
                    EventBus.getDefault().post(new EventWxPaySuccess());
                    ToastUtil.show("支付成功");
                    break;
                case -1:
                    ToastUtil.show("支付失败");
                    break;
                case -2:
                    ToastUtil.show("已取消");
                    break;
            }
        }
        finish();
    }
}