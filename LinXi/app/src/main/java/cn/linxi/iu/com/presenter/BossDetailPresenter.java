package cn.linxi.iu.com.presenter;
import android.content.Intent;
import android.util.Log;
import java.util.List;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.BossDetailMsg;
import cn.linxi.iu.com.model.BossDetailMsgEmployee;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IBossDetailPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IBossDetailView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/11/9.
 */
public class BossDetailPresenter implements IBossDetailPresenter {
    private IBossDetailView view;
    private Intent intent;
    public BossDetailPresenter(IBossDetailView view,Intent intent){
        this.view = view;
        this.intent = intent;
    }
    @Override
    public void getMsg(String option,int page) {
        if (!SystemUtils.networkState()){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        final String salendar = intent.getStringExtra(CommonCode.INTENT_COMMON);
        if (StringUtil.isNull(salendar)){
            view.showToast("请选择日期");
            return;
        }
        String operatId = intent.getStringExtra(CommonCode.INTENT_STATION_ID);
        final String url = HttpUrl.bossGetMsgDetail+ OkHttpUtil.getSign()+"&operat_id="+operatId+"&current_time="+salendar+"&option="+option+"&page="+page;
        OkHttpUtil.get(url, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
                Log.i(">>>>>>>>>",url);
//                ToastUtil.show(salendar+s);
                BaseResult result = GsonUtil.jsonToObject(s, BaseResult.class);
                if (result.success()){
                    BossDetailMsg msg = GsonUtil.jsonToObject(result.getResult(),BossDetailMsg.class);
                    if (msg != null){
                        if (msg.order_list != null){
                            List<BossDetailMsgEmployee> list = GsonUtil.jsonToList(msg.order_list,BossDetailMsgEmployee.class);
                            view.getMsgEmployee(msg,list);
                        }
                    } else {
                        view.showToast("信息有误");
                    }
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
}