package cn.linxi.iu.com.view.activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.EventLoginSuccess;
import cn.linxi.iu.com.presenter.BusinessLoginPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessLoginPresenter;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IBusinessLoginView;
import cn.linxi.iu.com.view.widget.MyDialog;
/**
 * Created by buzhiheng on 2016/7/26.
 */
public class BusinessLoginActivity extends AppCompatActivity implements View.OnClickListener ,IBusinessLoginView {
    private IBusinessLoginPresenter presenter;
    private final int TIME_OUT = 0X001;
    @Bind(R.id.et_loginstation_username)
    EditText etUsername;
    @Bind(R.id.et_loginstation_psd)
    EditText etPsd;
    private Dialog dialog;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TIME_OUT:
                    dialog.dismiss();
                    showToast("连接超时");
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_login);
        presenter = new BusinessLoginPresenter(this);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        dialog = MyDialog.getNoticeDialog(this,"正在登陆...");
        dialog.setCancelable(false);
        etUsername.setText(PrefUtil.getString(CommonCode.SP_IS_BUSINESS_USERNAME, ""));
        etPsd.setText(PrefUtil.getString(CommonCode.SP_IS_BUSINESS_PSD,""));
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
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
            case R.id.btn_loginbusy_commit:
                dialog.show();
                presenter.login(etUsername,etPsd,dialog);
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
    public void toStationActivity() {
        dialog.dismiss();
        Intent intent = new Intent(this,BusinessActivity.class);
        startActivity(intent);
        EventBus.getDefault().post(new EventLoginSuccess());
        finish();
    }
    @Override
    public void toBossActivity() {
        dialog.dismiss();
        Intent intent = new Intent(this,BossActivity.class);
        startActivity(intent);
        EventBus.getDefault().post(new EventLoginSuccess());
        finish();
    }
    @Override
    public void toInitPsdActivity() {
        dialog.dismiss();
        Intent intent = new Intent(this,BusinessInitPsdActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void timeOut() {
        handler.sendEmptyMessage(TIME_OUT);
    }
}