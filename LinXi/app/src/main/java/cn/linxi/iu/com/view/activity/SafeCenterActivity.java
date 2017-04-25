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
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.EventUserMsgChanged;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.view.widget.SafeCenterBottomDialog;
/**
 * Created by buzhiheng on 2016/8/11.
 */
public class SafeCenterActivity extends AppCompatActivity {
    @Bind(R.id.tv_safecenter_isbindpaypad)
    TextView tvIsBind;
    @Bind(R.id.tv_safecenter_bindphone)
    TextView tvBindPhone;
    private String phone;
    private int bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safecenter);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("安全中心");
    }
    private void initView() {
        phone = PrefUtil.getString(CommonCode.SP_USER_PHONE,"");
        tvBindPhone.setText(phone);
        bind = PrefUtil.getInt(CommonCode.SP_USER_PAYPSDISBIND,0);
        if (bind == 0){//未设置,初始化支付密码
            tvIsBind.setText("未设置");
        } else {//重置支付密码
            tvIsBind.setText("已设置");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        initView();
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
    public void onEvent(EventUserMsgChanged user){
        //用户资料更改,重新获取资料
        initView();
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.fl_safe_paypsd:
                showBottomDialog(R.id.fl_safe_paypsd);
                break;
            case R.id.fl_safe_bindphone:
                showBottomDialog(R.id.fl_safe_bindphone);
                break;
        }
    }
    private void showBottomDialog(final int btnId){
        final SafeCenterBottomDialog.Builder builder = new SafeCenterBottomDialog.Builder(this);
        if (btnId == R.id.fl_safe_paypsd){
            if (bind == 0){//未设置,初始化支付密码
                builder.setSureButton("设置支付密码");
            } else {//重置支付密码
                builder.setSureButton("重置支付密码");
            }
        } else if (btnId == R.id.fl_safe_bindphone){
            builder.setSureButton("修改绑定手机");
        }
        builder.setCancelButton("取消");
        builder.create(new SafeCenterBottomDialog.BottomDialogListener() {
            @Override
            public void sureClick() {
                if (btnId == R.id.fl_safe_paypsd){
                    Intent intent = new Intent(SafeCenterActivity.this,ChangePayPsdActivity.class);
                    startActivity(intent);
                } else if (btnId == R.id.fl_safe_bindphone){
                    Intent intent = new Intent(SafeCenterActivity.this,ChangeBindPhoneActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void cancelClick() {
            }
        });
    }
}