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
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.presenter.ChangeBindPhonePresenter;
import cn.linxi.iu.com.presenter.ipresenter.IChangeBindPhonePresenter;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IChangeBindPhoneView;

/**
 * Created by buzhiheng on 2016/8/11.
 */
public class ChangeBindPhoneActivity extends AppCompatActivity implements IChangeBindPhoneView {
    private IChangeBindPhonePresenter presenter;
    @Bind(R.id.et_changebindphone_code)
    EditText etCode;
    @Bind(R.id.tv_changebind_phone)
    TextView tvBindPhone;
    @Bind(R.id.btn_changebindphone_getcode)
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
        setContentView(R.layout.activity_changebind_phone);
        ButterKnife.bind(this);
        presenter = new ChangeBindPhonePresenter(this);
        initView();
    }

    private void initView() {
        presenter.getCode();
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("修改绑定手机");
        String phone = PrefUtil.getString(CommonCode.SP_USER_PHONE,"");
        tvBindPhone.setText("手机号码  "+phone);
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.btn_changebindphone_getcode:
                presenter.getCode();
                break;
            case R.id.btn_changebindphone_commit:
                presenter.changeBind(etCode);
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
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void toChangeNextActivity() {
        Intent intent = new Intent(this,ChangeBindPhoneNextActivity.class);
        startActivity(intent);
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
}