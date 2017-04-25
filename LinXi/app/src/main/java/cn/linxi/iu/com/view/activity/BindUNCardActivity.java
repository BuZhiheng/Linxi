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
import cn.linxi.iu.com.presenter.BindUnPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IBindUnPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IBindUnView;
/**
 * Created by buzhiheng on 2016/8/25.
 */
public class BindUNCardActivity extends AppCompatActivity implements IBindUnView{
    private IBindUnPresenter presenter;
    @Bind(R.id.et_binduncard_name)
    EditText etName;
    @Bind(R.id.et_binduncard_card)
    EditText etAccount;
    @Bind(R.id.et_binduncard_bank)
    EditText etBankName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_unioncard);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        presenter = new BindUnPresenter(this);
        ((TextView) findViewById(R.id.tv_titlebar_title)).setText("银行卡");
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.btn_binduncard_commit:
                presenter.bind(etName,etAccount,etBankName);
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
    public void bindSuccess() {
        finish();
    }
}