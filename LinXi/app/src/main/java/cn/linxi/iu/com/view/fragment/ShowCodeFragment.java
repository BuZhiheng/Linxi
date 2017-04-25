package cn.linxi.iu.com.view.fragment;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.presenter.QRCodeShowPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IQRCodeShowPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IQRCodeShowView;
import cn.linxi.iu.com.zxing.qrcode.Encoder;
/**
 * Created by buzhiheng on 2016/11/3.
 */
public class ShowCodeFragment extends Fragment implements IQRCodeShowView, View.OnClickListener {
    private IQRCodeShowPresenter presenter;
    private View view;
    @Bind(R.id.qrcode_image)
    ImageView mQRCodeImage;
    private Encoder mEncoder;
    private DecodeTask mDecodeTask;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            presenter.getQRCode(null);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null){
            return view;
        }
        view = inflater.inflate(R.layout.fragment_show_code,container,false);
        ButterKnife.bind(this, view);
        presenter = new QRCodeShowPresenter(this);
        initView();
        return view;
    }
    private void initView() {
        mQRCodeImage.setOnClickListener(this);
        final WindowManager manager = (WindowManager) getActivity().getSystemService(getActivity().WINDOW_SERVICE);
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
    public void onDestroy() {
//        presenter.stopTimer();
//        showToast("destroy");
        super.onDestroy();
    }
    @Override
    public void onPause() {
//        presenter.stopTimer();
//        showToast("pause");
        super.onPause();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.qrcode_image:
                presenter.getQRCode(null);
                break;
        }
    }
    private class DecodeTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            return mEncoder.encode(params[0]);
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mQRCodeImage.setImageBitmap(bitmap);
        }
    }
}
