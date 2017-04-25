package cn.linxi.iu.com.view.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import org.greenrobot.eventbus.EventBus;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.EventLoginSuccess;
import cn.linxi.iu.com.presenter.RegisterNextPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IRegisterNextPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IRegisterNextView;
/**
 * Created by buzhiheng on 2016/8/8.
 */
public class RegisterNextActivity extends AppCompatActivity implements IRegisterNextView{
    private IRegisterNextPresenter presenter;//
    @Bind(R.id.et_register_psd)
    EditText psd;
    @Bind(R.id.et_register_psdconfirm)
    EditText psdConfirm;
    @Bind(R.id.et_register_invite)
    EditText etInvite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_next);
        ButterKnife.bind(this);
        presenter = new RegisterNextPresenter(this);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.btn_register_commit:
                presenter.register(getIntent(),psd,psdConfirm,etInvite);
                break;
        }
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void registerSuccess() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        EventBus.getDefault().post(new EventLoginSuccess());
        finish();
    }
}