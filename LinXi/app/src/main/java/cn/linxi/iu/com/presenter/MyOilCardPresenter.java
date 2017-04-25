package cn.linxi.iu.com.presenter;
import java.util.List;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.SaleOilCard;
import cn.linxi.iu.com.presenter.ipresenter.IMyOilCardPresenter;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IMyOilCardView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/18.
 */
public class MyOilCardPresenter implements IMyOilCardPresenter {
    private IMyOilCardView view;
    public MyOilCardPresenter(IMyOilCardView view){
        this.view = view;
    }
    @Override
    public void getMyOilCard(final int page) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String id = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        String url = HttpUrl.getMyOilCardUrl+OkHttpUtil.getSign()+"&user_id="+id+"&page="+page;
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
                    List<SaleOilCard> list = GsonUtil.jsonToList(result.data.result,SaleOilCard.class);
                    if (list != null && list.size() > 0){
                        view.refreshRv(list);
                    } else {
                        if (page == 1){
                            view.setNoData();
                        }
                    }
                    return;
                }
                view.showToast(result.error);
            }
        });
    }
}