package cn.linxi.iu.com.view.activity;
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
import cn.linxi.iu.com.presenter.ChangePayPsdPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IChangePayPsdPresenter;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IChangePayPsdView;
/**
 * Created by buzhiheng on 2016/8/11.
 */
public class ChangePayPsdActivity extends AppCompatActivity implements IChangePayPsdView {
    private IChangePayPsdPresenter presenter;
    @Bind(R.id.et_changepaypsd_code)
    EditText etCode;
    @Bind(R.id.et_changepaypsd_psd)
    EditText etPsd;
    @Bind(R.id.et_changepaypsd_psdcomfirm)
    EditText etPsdConfirm;
    @Bind(R.id.btn_changepaypsd_getcode)
    Button btnCode;
    @Bind(R.id.tv_chanpaypsd_phone)
    TextView tvBindPhone;
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
        setContentView(R.layout.activity_change_paypsd);
        ButterKnife.bind(this);
        presenter = new ChangePayPsdPresenter(this);
        initView();
    }
    private void initView() {
        presenter.getCode();
        int bind = PrefUtil.getInt(CommonCode.SP_USER_PAYPSDISBIND, 1);
        if (bind == 0){//未设置,初始化支付密码
            ((TextView)findViewById(R.id.tv_titlebar_title)).setText("设置支付密码");
        }else {//重置支付密码
            ((TextView)findViewById(R.id.tv_titlebar_title)).setText("重置支付密码");
        }
        String phone = PrefUtil.getString(CommonCode.SP_USER_PHONE,"");
        tvBindPhone.setText("手机号码  "+phone);
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.btn_changepaypsd_getcode:
                presenter.getCode();
                break;
            case R.id.btn_changepaypsd_commit:
                presenter.change(etCode,etPsd,etPsdConfirm);
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
    public void changeSuccess() {
        finish();
    }
}