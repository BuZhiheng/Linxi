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

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.User;
import cn.linxi.iu.com.presenter.ForgetPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IForgetPresenter;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IForgetView;

/**
 * Created by buzhiheng on 2016/7/27.
 */
public class ForgetPsdActivity extends AppCompatActivity implements View.OnClickListener ,IForgetView{
    private IForgetPresenter presenter;
    @Bind(R.id.et_forget_username)
    EditText etUsername;
    @Bind(R.id.et_forget_code)
    EditText etCode;
    @Bind(R.id.btn_forget_getcode)
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
        setContentView(R.layout.activity_forget);
        presenter = new ForgetPresenter(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("找回密码");
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
            case R.id.btn_forget_getcode:
                presenter.getCode(etUsername);
                break;
            case R.id.btn_forget_commit:
                presenter.forget(etUsername, etCode);
                break;
        }
    }

    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
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
    public void toNextActivity(User user) {
        Intent intent = new Intent(this,ForgetPsdNextActivity.class);
        intent.putExtra(CommonCode.INTENT_REGISTER_USER,user);
        startActivity(intent);
        finish();
    }
}