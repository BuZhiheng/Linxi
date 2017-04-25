package cn.linxi.iu.com.view.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import com.umeng.analytics.MobclickAgent;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
/**
 * Created by buzhiheng on 2016/8/8.
 */
public class SplashActivity extends AppCompatActivity {
    private final int TO_LOGIN = 0X001;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TO_LOGIN:
                    Intent intent;
                    boolean isFirst = PrefUtil.getBoolean(CommonCode.SP_IS_STARTED,false);
                    if (!isFirst){
                        intent = new Intent(SplashActivity.this,GuideActivity.class);
                    } else {
                        boolean isLogin = PrefUtil.getBoolean(CommonCode.SP_IS_LOGIN,false);
                        boolean isLoginBusiness = PrefUtil.getBoolean(CommonCode.SP_IS_LOGIN_BUSINESS,false);
                        boolean isLoginBoss = PrefUtil.getBoolean(CommonCode.SP_IS_LOGIN_BOSS,false);
                        if (isLogin){
                            intent = new Intent(SplashActivity.this,MainActivity.class);
                        } else {
                            if (isLoginBusiness){
                                intent = new Intent(SplashActivity.this,BusinessActivity.class);
                            } else if (isLoginBoss){
                                intent = new Intent(SplashActivity.this,BossActivity.class);
                            } else {
                                intent = new Intent(SplashActivity.this,LoginControllerActivity.class);
                            }
                        }
                    }
                    startActivity(intent);
                    SplashActivity.this.finish();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }
    private void initView() {
        OkHttpUtil.executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    handler.sendEmptyMessage(TO_LOGIN);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
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
}