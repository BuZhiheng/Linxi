package cn.linxi.iu.com.view.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.ToastUtil;
public class MyCodeScanAvtivity extends AppCompatActivity implements QRCodeView.Delegate {
    private static final String TAG = MyCodeScanAvtivity.class.getSimpleName();
    private QRCodeView mQRCodeView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myqrcode_scan);
        mQRCodeView = (ZBarView) findViewById(R.id.zbarview);
        mQRCodeView.setDelegate(this);
        mQRCodeView.startSpot();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
    }
    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
//        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
//        vibrate();
//        mQRCodeView.startSpot();
        Intent intent = new Intent();
        intent.putExtra(CommonCode.INTENT_QRCODE, result);
        setResult(CommonCode.ACTIVITY_RESULT_CODE_QRCODE, intent);
//        if (result.startsWith("/enterprise/qrcode")){
//
//        } else {
//            ToastUtil.show("二维码无效");
//        }
        finish();
    }
    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
//        ToastUtil.show("请先为APP打开相机权限");
        finish();
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_mycode_back:
                finish();
                break;
//            case R.id.stop_spot:
//                mQRCodeView.stopSpot();
//                break;
//            case R.id.start_spot_showrect:
//                mQRCodeView.startSpotAndShowRect();
//                break;
//            case R.id.stop_spot_hiddenrect:
//                mQRCodeView.stopSpotAndHiddenRect();
//                break;
//            case R.id.show_rect:
//                mQRCodeView.showScanRect();
//                break;
//            case R.id.hidden_rect:
//                mQRCodeView.hiddenScanRect();
//                break;
//            case R.id.start_preview:
//                mQRCodeView.startCamera();
//                break;
//            case R.id.stop_preview:
//                mQRCodeView.stopCamera();
//                break;
//            case R.id.open_flashlight:
//                mQRCodeView.openFlashlight();
//                break;
//            case R.id.close_flashlight:
//                mQRCodeView.closeFlashlight();
//                break;
//            case R.id.scan_barcode:
//                mQRCodeView.changeToScanBarcodeStyle();
//                break;
//            case R.id.scan_qrcode:
//                mQRCodeView.changeToScanQRCodeStyle();
//                break;
        }
    }
}