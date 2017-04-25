package cn.linxi.iu.com.presenter;
import android.util.Log;
import java.util.List;
import cn.linxi.iu.com.model.BalanceDetail;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IOilDetailPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IOilDetailView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/26.
 */
public class OilDetailPresenter implements IOilDetailPresenter {
    private IOilDetailView view;
    public OilDetailPresenter(IOilDetailView view){
        this.view = view;
    }
    @Override
    public void getOilDetailList(int page) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        int userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0);
        String url = HttpUrl.getBalanceList+OkHttpUtil.getSign()+"&query=card&user_id="+userId+"&page="+page;
        Log.i(">>>>>>>>>>>>>>>>>>>",url);
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
                    List<BalanceDetail> list = GsonUtil.jsonToList(result.data.result,BalanceDetail.class);
                    view.getDetailSuccess(list);
                }
            }
        });
    }
}