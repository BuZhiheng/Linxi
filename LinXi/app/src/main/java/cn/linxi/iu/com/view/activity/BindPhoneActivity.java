package cn.linxi.iu.com.view.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import org.greenrobot.eventbus.EventBus;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.EventLoginSuccess;
import cn.linxi.iu.com.presenter.BindPhonePresenter;
import cn.linxi.iu.com.presenter.ipresenter.IBindPhonePresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IBindPhoneView;
/**
 * Created by buzhiheng on 2016/7/27.
 */
public class BindPhoneActivity extends AppCompatActivity implements IBindPhoneView{
    private IBindPhonePresenter presenter;
    @Bind(R.id.et_bindphone_phone)
    EditText etPhone;
    @Bind(R.id.et_bindphone_code)
    EditText etCode;
    @Bind(R.id.btn_bindphone_getcode)
    Button btnCode;
    private String time;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x001:
                    btnCode.setText(time+"秒后重新获取");
                    break;
                case 0x002:
                    btnCode.setText("重新发送");
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        presenter = new BindPhonePresenter(this);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("绑定手机");
        ((TextView)findViewById(R.id.tv_titlebar_right)).setText("跳过");
//        findViewById(R.id.tv_titlebar_right).setVisibility(View.GONE);
        presenter.showJump(getIntent());
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.tv_titlebar_right:
                bindSuccess();
                break;
            case R.id.btn_bindphone_getcode:
                presenter.getCode(etPhone);
                break;
            case R.id.btn_bindphone_commit:
                presenter.bind(etPhone, etCode);
                break;
        }
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void bindSuccess() {
        Intent intentMain = new Intent(this,MainActivity.class);
        startActivity(intentMain);
        EventBus.getDefault().post(new EventLoginSuccess());
        finish();
    }
    @Override
    public void refreshCodeButton(String time) {
        this.time = time;
        handler.sendEmptyMessage(0x001);
    }
    @Override
    public void setCodeBtnCanClick() {
        btnCode.setClickable(true);
        handler.sendEmptyMessage(0x002);
    }
    @Override
    public void setCodeBtnCanNotClick() {
        btnCode.setClickable(false);
    }
    @Override
    public void setJumpBtn() {
        findViewById(R.id.tv_titlebar_right).setVisibility(View.VISIBLE);
    }
}