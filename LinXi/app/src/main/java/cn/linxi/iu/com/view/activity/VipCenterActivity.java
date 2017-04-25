package cn.linxi.iu.com.view.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;

/**
 * Created by buzhiheng on 2016/8/9.
 */
public class VipCenterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_center);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_vip_back:
                finish();
                break;
            case R.id.btn_vipcenter_buyoil:
                setResult(CommonCode.ACTIVITY_RESULT_CODE_MINE);
                finish();
                break;
            case R.id.fl_vipcenter_reward:
                Intent intentService = new Intent(this,WebViewActivity.class);
                intentService.putExtra(CommonCode.INTENT_WEBVIEW_URL, HttpUrl.URL_VIP);
                intentService.putExtra(CommonCode.INTENT_COMMON,"会员说明");
                startActivity(intentService);
                break;
        }
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