package cn.linxi.iu.com.view.activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.EventLoginSuccess;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.TencentUser;
import cn.linxi.iu.com.presenter.LoginPresenter;
import cn.linxi.iu.com.util.QQLoginListener;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.ILoginView;
import cn.linxi.iu.com.view.widget.MyDialog;
/**
 * Created by buzhiheng on 2016/7/26.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ILoginView {
    private LoginPresenter presenter;
    private final int TIME_OUT = 0X003;
    private final int HANDLER_CANCLICK = 0X002;
    private final int HANDLER_REFRESH = 0X001;
    private Tencent tencent;
    private QQLoginListener listener;
    @Bind(R.id.et_login_username)
    EditText etUsername;
    @Bind(R.id.et_login_psd)
    EditText etPsd;
    @Bind(R.id.tv_login_qq)
    TextView tvQQ;
    @Bind(R.id.tv_login_wx)
    TextView tvWX;
    @Bind(R.id.btn_login_getcode)
    Button btnGetCode;
    private Dialog dialog;
    private String time;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TIME_OUT:
                    dialog.dismiss();
                    showToast("连接超时");
                    break;
                case HANDLER_REFRESH:
                    btnGetCode.setText(time+"秒后重新获取");
                    break;
                case HANDLER_CANCLICK:
                    btnGetCode.setText("重新发送");
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        presenter = new LoginPresenter(this);
        initView();
    }
    private void initView() {
        dialog = MyDialog.getNoticeDialog(this,"请稍后...");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, listener);
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        dialog.dismiss();
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_login_back:
                finish();
                break;
            case R.id.tv_login_forget:
                Intent intentForget = new Intent(this,ForgetPsdActivity.class);
                startActivity(intentForget);
                break;
            case R.id.btn_login_commit:
                dialog.show();
                presenter.login(CommonCode.LOGIN_BY_USERNAME, etUsername, etPsd, dialog);
                break;
            case R.id.tv_login_register:
                Intent intentRegist = new Intent(this,RegisterActivity.class);
                startActivity(intentRegist);
                break;
            case R.id.btn_login_getcode:
                presenter.getCode(etUsername);
                break;
            case R.id.ll_login_qq:
                presenter.checkLoginType(CommonCode.LOGIN_BY_QQ);
                break;
            case R.id.ll_login_wx:
                presenter.checkLoginType(CommonCode.LOGIN_BY_WX);
                break;
            case R.id.ll_login_service:
                Intent intentService = new Intent(this,WebViewActivity.class);
                intentService.putExtra(CommonCode.INTENT_WEBVIEW_URL, HttpUrl.URL_AGREEMENT);
                intentService.putExtra(CommonCode.INTENT_COMMON,"用户协议");
                startActivity(intentService);
                break;
            default:break;
        }
    }
    @Override
    public void showToast(String toast) {
        dialog.dismiss();
        ToastUtil.show(toast);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        dialog.dismiss();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventLoginSuccess loginSuccess){//登陆成功关闭activity
        finish();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TencentUser user){
        //微信登录
//        ToastUtil.show(user.openid);
        presenter.loginByService(user, CommonCode.LOGIN_BY_WX);
    }
    @Override
    public void toMainActivity() {
        dialog.dismiss();
        Intent intentMain = new Intent(this,MainActivity.class);
        startActivity(intentMain);
        EventBus.getDefault().post(new EventLoginSuccess());
        finish();
    }
    @Override
    public void toBindPhoneActivity() {
        dialog.dismiss();
        Intent intentBind = new Intent(this,BindPhoneActivity.class);
        intentBind.putExtra(CommonCode.INTENT_BIND_PHONE_FROM,CommonCode.INTENT_BIND_PHONE_FROM);
        startActivity(intentBind);
        finish();
    }
    @Override
    public void setTimeOut() {
        handler.sendEmptyMessage(TIME_OUT);
    }
    @Override
    public void showQQDialog(String msg) {
        AlertDialog dialog = MyDialog.getAlertDialog(this, msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loginByQQ();
                dialog.dismiss();
            }
        });
    }
    @Override
    public void showWXDialog(String msg) {
        AlertDialog dialog = MyDialog.getAlertDialog(this, msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loginByWX();
                dialog.dismiss();
            }
        });
    }
    @Override
    public void loginByQQ() {
        dialog.show();
        tencent = Tencent.createInstance(CommonCode.APP_ID_QQ, this);
        listener = new QQLoginListener(tencent,this);
        //如果session无效，就开始登录
        if (!tencent.isSessionValid()) {
            //开始qq授权登录
            tencent.login(this, "all", listener);
        }
    }
    @Override
    public void loginByWX() {
        dialog.show();
        // send oauth request
        IWXAPI api = WXAPIFactory.createWXAPI(this, CommonCode.APP_ID_WX, true);
        api.registerApp(CommonCode.APP_ID_WX);
        if (!api.isWXAppInstalled()) {
            ToastUtil.show("您还未安装微信客户端");
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        api.sendReq(req);
    }
    @Override
    public void setLastUsername(String username) {
        etUsername.setText(username);
//        etPsd.setText(PrefUtil.getString(CommonCode.SP_USER_PASSWORD, ""));
    }
    @Override
    public void setLastLoginQQ() {
        tvQQ.setText("上次登录...");
        tvWX.setText("微信");
    }
    @Override
    public void setLastLoginWX() {
        tvQQ.setText("QQ登录");
        tvWX.setText("上次登录...");
    }
    @Override
    public void setCodeBtnCanNotClick() {
        btnGetCode.setClickable(false);
    }
    @Override
    public void refreshCodeButton(String s) {
        this.time = s;
        handler.sendEmptyMessage(HANDLER_REFRESH);
    }
    @Override
    public void setCodeBtnCanClick() {
        btnGetCode.setClickable(true);
        handler.sendEmptyMessage(HANDLER_CANCLICK);
    }
}