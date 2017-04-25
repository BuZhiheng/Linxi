package cn.linxi.iu.com.presenter;
import java.util.List;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.BusinessIndexData;
import cn.linxi.iu.com.model.BusinessIndexDataItem;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessIndexOilFrmPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.util.TimeUtil;
import cn.linxi.iu.com.view.iview.IBusinessIndexView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2017/4/14.
 */
public class BusinessIndexGoodsFrmPresenter implements IBusinessIndexOilFrmPresenter {
    private IBusinessIndexView view;
    public BusinessIndexGoodsFrmPresenter(IBusinessIndexView view){
        this.view = view;
        view.setStime(TimeUtil.getTime(TimeUtil.FORMAT_MMDD));
        view.setEtime(TimeUtil.getTime(TimeUtil.FORMAT_MMDD));
    }
    @Override
    public void getData(String stime, String etime) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        int operatId = PrefUtil.getInt(CommonCode.SP_USER_OPERA_ID, 0);
        String url = HttpUrl.businessGetUser + OkHttpUtil.getSign()+"&operat_id="+operatId+"&stime="+stime+"&etime="+etime+"&type=1";
        OkHttpUtil.get(url, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
                BaseResult result = GsonUtil.jsonToObject(s, BaseResult.class);
                if (result.success()) {
                    BusinessIndexData data = GsonUtil.jsonToObject(result.getResult(),BusinessIndexData.class);
                    view.setData(data);
                    if (data.list != null){
                        List<BusinessIndexDataItem> list = GsonUtil.jsonToList(data.list,BusinessIndexDataItem.class);
                        view.setDataItem(list);
                    }
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
}