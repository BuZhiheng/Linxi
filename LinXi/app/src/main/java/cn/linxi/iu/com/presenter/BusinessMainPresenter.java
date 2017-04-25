package cn.linxi.iu.com.presenter;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.BusinessWorkout;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.OperatUser;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessMainPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IBusinessMainView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/9/5.
 */
public class BusinessMainPresenter implements IBusinessMainPresenter {
    private IBusinessMainView view;
    public BusinessMainPresenter(IBusinessMainView view){
        this.view = view;
    }
    @Override
    public void getOperateUser() {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String operatId = PrefUtil.getInt(CommonCode.SP_USER_OPERA_ID,0)+"";
        String url = HttpUrl.businessGetUser+OkHttpUtil.getSign()+"&operat_id="+operatId;
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
                    OperatUser user = GsonUtil.jsonToObject(result.getResult(),OperatUser.class);
                    view.getUserSuccess(user);
//                    view.showToast(user.username);
                }
            }
        });
    }
    @Override
    public void checkWorkOut() {
        String operatId = PrefUtil.getInt(CommonCode.SP_USER_OPERA_ID,0)+"";
        String url = HttpUrl.businessWorkOut+OkHttpUtil.getSign()+"&operat_id="+operatId;
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
                    BusinessWorkout workout = GsonUtil.jsonToObject(result.getResult(),BusinessWorkout.class);
                    if (workout.is_settle != null && workout.is_settle == 0){
                        view.cantWorkOut();
                    } else {
                        view.workOut();
                    }
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
}