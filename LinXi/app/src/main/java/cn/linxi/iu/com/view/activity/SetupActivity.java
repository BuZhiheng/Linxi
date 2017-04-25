package cn.linxi.iu.com.view.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.UpdateMsg;
import cn.linxi.iu.com.presenter.SetupPresenter;
import cn.linxi.iu.com.presenter.UpdatePresenter;
import cn.linxi.iu.com.presenter.ipresenter.ISetupPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IUpdatePresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.ISetupView;
import cn.linxi.iu.com.view.iview.IUpdateView;
import cn.linxi.iu.com.view.widget.UpdateDialog;
/**
 * Created by buzhiheng on 2016/8/3.
 */
public class SetupActivity extends AppCompatActivity implements ISetupView,IUpdateView{
    private ISetupPresenter presenter;
    private IUpdatePresenter updatePresenter;
    @Bind(R.id.iv_setup_vioce)
    ImageView ivVoice;
    @Bind(R.id.iv_setup_vib)
    ImageView ivVib;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        ButterKnife.bind(this);
        presenter = new SetupPresenter(this);
        updatePresenter = new UpdatePresenter(this);
        initView();
    }
    private void initView() {
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("设置");
        presenter.init();
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
    public void onClick(View v){
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.fl_setup_update:
                updatePresenter.getUpdateSetting();
                break;
            case R.id.fl_setup_voice:
                presenter.onVoiceClick();
                break;
            case R.id.fl_setup_vib:
                presenter.onVibClick();
                break;
            case R.id.fl_setup_aboutus:
                Intent intentAboutUs = new Intent(this,AboutUsActivity.class);
                startActivity(intentAboutUs);
                break;
        }
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void showUpdate(UpdateMsg msg) {
        UpdateDialog dialog = new UpdateDialog(this,msg);
        dialog.show();
    }
    @Override
    public void setVoiceOn() {
        ivVoice.setImageResource(R.drawable.ic_setup_on);
    }
    @Override
    public void setVoiceOff() {
        ivVoice.setImageResource(R.drawable.ic_setup_off);
    }
    @Override
    public void setVibOn() {
        ivVib.setImageResource(R.drawable.ic_setup_on);
    }
    @Override
    public void setVibOff() {
        ivVib.setImageResource(R.drawable.ic_setup_off);
    }
}