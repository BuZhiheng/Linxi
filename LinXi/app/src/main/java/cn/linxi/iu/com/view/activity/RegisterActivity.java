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
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.EventLoginSuccess;
import cn.linxi.iu.com.model.User;
import cn.linxi.iu.com.presenter.RegisterPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IRegisterPresenter;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IRegisterView;
/**
 * Created by buzhiheng on 2016/7/27.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener,IRegisterView{
    private IRegisterPresenter presenter;
    @Bind(R.id.et_register_username)
    EditText etUsername;
    @Bind(R.id.et_register_code)
    EditText etCode;
    @Bind(R.id.btn_register_getcode)
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
        setContentView(R.layout.activity_register);
        presenter = new RegisterPresenter(this);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }
    private void initView() {
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("注册");
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
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.btn_register_getcode:
                presenter.getCode(etUsername);
                break;
            case R.id.btn_register_commit:
                presenter.register(etUsername, etCode);
                break;
        }
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void toNextActivity(User user) {
        Intent intent = new Intent(this,RegisterNextActivity.class);
        intent.putExtra(CommonCode.INTENT_REGISTER_USER,user);
        startActivity(intent);
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Override
    public void setCodeBtnCanNotClick() {
        btnCode.setClickable(false);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventLoginSuccess loginSuccess){
        //eventbus事件方法,登录成功
        finish();
    }
}