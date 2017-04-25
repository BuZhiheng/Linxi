package cn.linxi.iu.com.presenter.ipresenter;
import android.content.Intent;
/**
 * Created by buzhiheng on 2016/8/29.
 */
public interface IQRCodeShowPresenter {
    void getQRCode(Intent intent);
    void setTimer();
    void stopTimer();
}