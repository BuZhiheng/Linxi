package cn.linxi.iu.com.presenter;

import java.util.List;

import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.CouponCard;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.ICouponPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.ICouponView;
import rx.Subscriber;

/**
 * Created by buzhiheng on 2016/8/29.
 */
public class CouponPresenter implements ICouponPresenter {
    private ICouponView view;
    public CouponPresenter(ICouponView view){
        this.view = view;
    }
    @Override
    public void getCardList(int page) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        int userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0);
        String url = HttpUrl.userSignCardList+OkHttpUtil.getSign()+"&user_id="+userId+"&page="+page;
        OkHttpUtil.get(url, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
//                ToastUtil.show(s);
                BaseResult result = GsonUtil.jsonToObject(s,BaseResult.class);
                if (result.success()){
                    List<CouponCard> list = GsonUtil.jsonToList(result.data.result,CouponCard.class);
                    view.getListSuccess(list);
                }
            }
        });
    }
}