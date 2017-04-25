package cn.linxi.iu.com.presenter;
import android.util.Log;
import java.util.List;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.BossMsg;
import cn.linxi.iu.com.model.BossMsgEmployeeDetail;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IBossPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.util.TimeUtil;
import cn.linxi.iu.com.view.iview.IBossView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/11/9.
 */
public class BossPresenter implements IBossPresenter {
    private IBossView view;
    public BossPresenter(IBossView view){
        this.view = view;
    }
    @Override
    public void initSalendar() {
        String data = TimeUtil.getTime(TimeUtil.FORMAT_YYMMDD);
        view.setSalendar(data);
        getMsg(data,1);
    }
    @Override
    public void getMsg(final String salendar,int page) {
        if (!SystemUtils.networkState()){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (StringUtil.isNull(salendar)){
            view.showToast("请选择日期");
            return;
        }
        String operatId = PrefUtil.getInt(CommonCode.SP_USER_OPERA_ID, 0)+"";
        final String url = HttpUrl.bossGetMsg+ OkHttpUtil.getSign()+"&operat_id="+operatId+"&current_time="+salendar+"&page="+page;
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
                    BossMsg msg = GsonUtil.jsonToObject(result.getResult(),BossMsg.class);
                    if (msg != null){
                        if (msg.profit_amount != null){
                            view.getProfitSuccess(msg.profit_amount);
                        }
                        if (msg.today_amount != null){
                            view.getTodayMsgAmountSuccess(msg.today_amount);
                        }
                        if (msg.today_purchase != null){
                            view.getTodayMsgPurchaseSuccess(msg.today_purchase);
                        }
                        if (msg.user_list != null){
                            List<BossMsgEmployeeDetail> list = GsonUtil.jsonToList(msg.user_list,BossMsgEmployeeDetail.class);
                            view.getMsgEmployee(list);
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