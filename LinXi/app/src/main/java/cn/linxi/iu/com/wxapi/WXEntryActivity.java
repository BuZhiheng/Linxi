package cn.linxi.iu.com.wxapi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.util.WXLogin;
public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
    private IWXAPI api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, CommonCode.APP_ID_WX, true);
        api.registerApp(CommonCode.APP_ID_WX);
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
//        ToastUtil.show(req.toString() + "onReq");
        switch (req.getType()) {
        case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
            break;
        case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
            break;
        default:
            break;
        }
    }
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.getType()){
            case 1://微信登陆
                if (resp.errCode == BaseResp.ErrCode.ERR_OK){
                    SendAuth.Resp r = (SendAuth.Resp) resp;
                    if (r.code != null){
                        WXLogin.wxLogin(r.code);
                    }
                } else {
                    on(resp);
                }
                break;
            case 2://微信分享
                on(resp);
                break;
        }
        finish();
    }
    private void on(BaseResp resp){
        String result;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "分享成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "已取消";
                break;
            default:
                result = "操作失败";
                break;
        }
        ToastUtil.show(result);
    }
}