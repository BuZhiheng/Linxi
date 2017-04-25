package cn.linxi.iu.com.view.activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.umeng.analytics.MobclickAgent;
import org.greenrobot.eventbus.EventBus;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.MyQrCode;
import cn.linxi.iu.com.zxing.qrcode.QRCodeSupport;
/**
 * 扫描二维码
 */
public class QRCodeScanActivity extends AppCompatActivity{
    private QRCodeSupport mQRCodeScanSupport;
    private final Handler mHandler = new Handler();
    private Animation animation;
    private final Runnable mDelayAutoTask = new Runnable() {
        @Override
        public void run() {
            mQRCodeScanSupport.startAuto(500);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview_view);
        mQRCodeScanSupport = new QRCodeSupport(surfaceView, new QRCodeSupport.OnResultListener() {
            @Override
            public void onScanResult(String notNullResult) {
                Log.i(">>>>>>>>>",notNullResult);
                EventBus.getDefault().post(new MyQrCode(notNullResult));
                finish();
            }
        });
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_qrscan_bounds);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ll.getLayoutParams();
        params.width = (int) (0.6*width);
        params.height = (int) (0.6*width);
        ll.setLayoutParams(params);
        animation = AnimationUtils.loadAnimation(this,R.anim.anim_qrcodeline_trans);
        findViewById(R.id.iv_qrcode_line).setAnimation(animation);
        animation.start();
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_qrcode_back:
                finish();
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        mQRCodeScanSupport.onResume();
        mHandler.postDelayed(mDelayAutoTask, 500);
        animation.reset();
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        mQRCodeScanSupport.onPause();
        mHandler.removeCallbacks(mDelayAutoTask);
        animation.cancel();
    }
}