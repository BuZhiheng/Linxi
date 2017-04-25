package cn.linxi.iu.com.view.activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.presenter.ForgetNextPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IForgetNextPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IForgetNextView;
/**
 * Created by buzhiheng on 2016/7/27.
 */
public class ForgetPsdNextActivity extends AppCompatActivity implements View.OnClickListener ,IForgetNextView {
    private IForgetNextPresenter presenter;
    @Bind(R.id.et_forget_psd)
    EditText psd;
    @Bind(R.id.et_forget_psdconfirm)
    EditText psdConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_next);
        ButterKnife.bind(this);
        presenter = new ForgetNextPresenter(this);
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
            case R.id.btn_forget_commit:
                presenter.forget(getIntent(), psd,psdConfirm);
                break;
        }
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void findPsdSuccess() {
        this.finish();
    }
}