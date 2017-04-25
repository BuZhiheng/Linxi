package cn.linxi.iu.com.view.activity;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.umeng.analytics.MobclickAgent;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.presenter.FirstRegisterPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IFirstRegisterPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IFirstRegisterView;
import cn.linxi.iu.com.view.widget.MyDialog;
/**
 * Created by buzhiheng on 2016/9/8.
 */
public class FirstRegisterEnvelopActivity extends Activity implements IFirstRegisterView {
    private IFirstRegisterPresenter presenter;
    @Bind(R.id.iv_firstregister_envelop)
    ImageView ivGetEnvelop;
    private Dialog dialog;
    private Animation anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstregister_envelop);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        presenter = new FirstRegisterPresenter(this);
        dialog = MyDialog.getNoticeDialog(this,"正在打开红包...");
        dialog.setCancelable(false);
        anim = AnimationUtils.loadAnimation(this, R.anim.anim_envelop_rotate);
        AccelerateInterpolator lir = new AccelerateInterpolator();
        anim.setInterpolator(lir);
        ivGetEnvelop.startAnimation(anim);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_firstregister_envelop:
                dialog.show();
                presenter.getEnvelop();
                break;
            case R.id.iv_firstregister_finish:
                dialog.dismiss();
                finish();
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
    public void showEnvelopResult(int id) {
        ivGetEnvelop.clearAnimation();
        ivGetEnvelop.setImageResource(id);
        ivGetEnvelop.setClickable(false);
    }
}