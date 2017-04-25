package cn.linxi.iu.com.view.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.EventLoginSuccess;
import cn.linxi.iu.com.util.WindowUtil;
/**
 * Created by buzhiheng on 2016/7/25.
 */
public class LoginControllerActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.tv_login_controller_vcode)
    TextView tvVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_controll);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }
    private void initView() {
        findViewById(R.id.btn_login_driver).setOnClickListener(this);
        findViewById(R.id.btn_login_business).setOnClickListener(this);
        tvVersion.setText("版本号:V "+ WindowUtil.getAppVersionName());
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventLoginSuccess loginSuccess){//登录成功
        finish();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_driver:
                Intent intentDriver = new Intent(this,LoginActivity.class);
                startActivity(intentDriver);
                break;
            case R.id.btn_login_business:
                Intent intentBusy = new Intent(this,BusinessLoginActivity.class);
                startActivity(intentBusy);
                break;
            default:break;
        }
    }
}
