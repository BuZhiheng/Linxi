package cn.linxi.iu.com.presenter;
import android.content.Intent;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.MyQrCode;
import cn.linxi.iu.com.presenter.ipresenter.IQRCodeShowPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IQRCodeShowView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/29.
 */
public class QRCodeShowPresenter implements IQRCodeShowPresenter {
    private IQRCodeShowView view;
    private Timer time;
    public QRCodeShowPresenter(IQRCodeShowView view){
        this.view = view;
        time = new Timer();
    }
    @Override
    public void getQRCode(Intent intent) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        int userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0);
        String url = HttpUrl.getQRCode+ OkHttpUtil.getSign()+"&user_id="+userId;
        if (intent != null && intent.getIntExtra(CommonCode.INTENT_COMMON,-1) != -1){
            url = url+"&envelope_id="+intent.getIntExtra(CommonCode.INTENT_COMMON,-1);
        }
        Log.i(">>>>>>>>>>>",url);
        OkHttpUtil.get(url, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
                BaseResult result = GsonUtil.jsonToObject(s,BaseResult.class);
                if (result.success()){
                    MyQrCode code = GsonUtil.jsonToObject(result.getResult(),MyQrCode.class);
                    view.showQRCode(code.link);
//                    view.showToast(code.link);
                }
            }
        });
    }
    @Override
    public void setTimer() {
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                view.timeCome();
            }
        },0,1000*5*60L);
        //第一个参数是要操作的方法，第二个参数是要设定延迟的时间，第三个参
        //数是周期的设定，每隔多长时间执行该操作。
    }
    @Override
    public void stopTimer() {
        time.cancel();
    }
}