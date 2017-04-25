package cn.linxi.iu.com.view.activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import cn.linxi.iu.com.R;
/**
 * Created by buzhiheng on 2016/10/12.
 */
public class WaitForOpenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitfor_open);
        initView();
    }
    private void initView() {
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("敬请期待");
    }
    public void onClick(View v){
        finish();
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