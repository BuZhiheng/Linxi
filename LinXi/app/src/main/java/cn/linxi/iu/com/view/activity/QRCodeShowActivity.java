package cn.linxi.iu.com.view.activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.presenter.QRCodeShowPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IQRCodeShowPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IQRCodeShowView;
import cn.linxi.iu.com.zxing.qrcode.Encoder;
/**
 * 生成二维码并展示
 */
public class QRCodeShowActivity extends AppCompatActivity implements IQRCodeShowView{
    private IQRCodeShowPresenter presenter;
    private ImageView mQRCodeImage;
    private Encoder mEncoder;
    private DecodeTask mDecodeTask;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            presenter.getQRCode(getIntent());
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        presenter = new QRCodeShowPresenter(this);
        initView();
    }
    private void initView() {
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("我要加油");
        mQRCodeImage = (ImageView) findViewById(R.id.qrcode_image);
        final WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        final Display display = manager.getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        final int width = displaySize.x;
        final int height = displaySize.y;
        final int dimension = width < height ? width : height;
        mEncoder = new Encoder.Builder()
                .setBackgroundColor(0xFFFFFF)
                .setCodeColor(0xFF000000)
                .setOutputBitmapPadding(0)
                .setOutputBitmapWidth(dimension)
                .setOutputBitmapHeight(dimension)
                .build();
        presenter.setTimer();
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void showQRCode(String msg) {
        mDecodeTask = new DecodeTask();
        mDecodeTask.execute(msg);
    }
    @Override
    public void timeCome() {
        handler.sendEmptyMessage(0x001);
    }
    @Override
    protected void onDestroy() {
        presenter.stopTimer();
        super.onDestroy();
    }
    private class DecodeTask extends AsyncTask<String, Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(String... params) {
            return mEncoder.encode(params[0]);
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mQRCodeImage.setImageBitmap(bitmap);
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
        presenter.stopTimer();
        MobclickAgent.onPause(this);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
        }
    }
}