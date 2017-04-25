package cn.linxi.iu.com.view.activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import cn.linxi.iu.com.presenter.BusinessInitPsdPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessInitPsdPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IBusinessInitPsdView;
import cn.linxi.iu.com.view.widget.MyDialog;
/**
 * Created by buzhiheng on 2016/9/1.
 */
public class BusinessInitPsdActivity extends AppCompatActivity implements IBusinessInitPsdView{
    private IBusinessInitPsdPresenter presenter;
    private final int TIME_OUT = 0x001;
    @Bind(R.id.et_initpsd_psd)
    EditText etPsd;
    @Bind(R.id.et_initpsd_psdconfirm)
    EditText etPsdConfirm;
    private Dialog dialog;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TIME_OUT:
                    dialog.dismiss();
                    showToast("连接超时");
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_initpsd);
        ButterKnife.bind(this);
        presenter = new BusinessInitPsdPresenter(this);
        initView();
    }
    private void initView() {
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("修改初始密码");
        dialog = MyDialog.getNoticeDialog(this,"正在修改...");

    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.btn_initpsd_commit:
                dialog.show();
                presenter.init(etPsd, etPsdConfirm,dialog);
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
        dialog.dismiss();
        ToastUtil.show(toast);
    }
    @Override
    public void initSuccess() {
        dialog.dismiss();
        Intent intent = new Intent(this,BusinessActivity.class);
        startActivity(intent);
        finish();
        EventBus.getDefault().post(new EventLoginSuccess());
    }
    @Override
    public void timeOut() {
        handler.sendEmptyMessage(TIME_OUT);
    }
}
