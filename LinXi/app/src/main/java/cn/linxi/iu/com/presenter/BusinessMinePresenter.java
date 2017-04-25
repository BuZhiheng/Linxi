package cn.linxi.iu.com.presenter;
import android.util.Log;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.OperateMine;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessMinePresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IBusinessMineView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/9/5.
 */
public class BusinessMinePresenter implements IBusinessMinePresenter {
    private IBusinessMineView view;
    public BusinessMinePresenter(IBusinessMineView view){
        this.view = view;
    }
    @Override
    public void getUser() {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String operatId = PrefUtil.getInt(CommonCode.SP_USER_OPERA_ID,0)+"";
        String url = HttpUrl.businessGetUserMine+OkHttpUtil.getSign()+"&operat_id="+operatId;
        Log.i(">>>",url);
        OkHttpUtil.get(url, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
//                view.showToast(s);
                BaseResult result = GsonUtil.jsonToObject(s,BaseResult.class);
                if (result.success()){
                    OperateMine user = GsonUtil.jsonToObject(result.getResult(),OperateMine.class);
                    view.setUser(user);
//                    view.showToast(user.username);
                }
            }
        });
    }
}