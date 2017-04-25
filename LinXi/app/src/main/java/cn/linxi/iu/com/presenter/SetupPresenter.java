package cn.linxi.iu.com.presenter;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.presenter.ipresenter.ISetupPresenter;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.view.iview.ISetupView;
/**
 * Created by buzhiheng on 2016/9/14.
 */
public class SetupPresenter implements ISetupPresenter {
    private ISetupView view;
    public SetupPresenter(ISetupView view){
        this.view = view;
    }
    @Override
    public void init() {
        int voice = PrefUtil.getInt(CommonCode.SP_APP_VOICE,0);
        if (voice == 0){
            view.setVoiceOff();
        } else {
            view.setVoiceOn();
        }
        int vib = PrefUtil.getInt(CommonCode.SP_APP_VIB,0);
        if (vib == 0){
            view.setVibOff();
        } else {
            view.setVibOn();
        }
    }
    @Override
    public void onVoiceClick() {
        int code = PrefUtil.getInt(CommonCode.SP_APP_VOICE,0);
        if (code == 0){
            view.setVoiceOn();
            PrefUtil.putInt(CommonCode.SP_APP_VOICE, 1);
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(((Context)view).getApplicationContext(), notification);
            r.play();
        } else {
            view.setVoiceOff();
            PrefUtil.putInt(CommonCode.SP_APP_VOICE, 0);
        }
    }
    @Override
    public void onVibClick() {
        int code = PrefUtil.getInt(CommonCode.SP_APP_VIB,0);
        if (code == 0){
            view.setVibOn();
            PrefUtil.putInt(CommonCode.SP_APP_VIB, 1);
            Vibrator vibrator = (Vibrator)((Context)view).getSystemService(Context.VIBRATOR_SERVICE);
            long [] pattern = {100,400,100,400};   // 停止 开启 停止 开启
            vibrator.vibrate(pattern,-1);
        } else {
            view.setVibOff();
            PrefUtil.putInt(CommonCode.SP_APP_VIB, 0);
        }
    }
}