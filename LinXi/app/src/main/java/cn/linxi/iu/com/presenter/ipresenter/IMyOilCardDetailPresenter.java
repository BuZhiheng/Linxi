package cn.linxi.iu.com.presenter.ipresenter;
import android.content.Intent;
/**
 * Created by buzhiheng on 2016/9/23.
 */
public interface IMyOilCardDetailPresenter {
    void getCardMsg(Intent intent);
    void checkPsdBind();
    void sale(String psd);
}