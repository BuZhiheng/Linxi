package cn.linxi.iu.com.presenter;
import android.util.Log;
import java.util.List;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.OperateIncome;
import cn.linxi.iu.com.model.OperateIncomeItem;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessIncomePresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IBusinessIncomeView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/12/4.
 */
public class BusinessIncomePresenter implements IBusinessIncomePresenter {
    private IBusinessIncomeView view;
    public BusinessIncomePresenter(IBusinessIncomeView view){
        this.view = view;
    }
    @Override
    public void getIncomeInfo() {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String operatId = PrefUtil.getInt(CommonCode.SP_USER_OPERA_ID, 0)+"";
        String url = HttpUrl.businessGetIncome+ OkHttpUtil.getSign()+"&operat_id="+operatId;
        Log.i(">>>", url);
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
                BaseResult result = GsonUtil.jsonToObject(s, BaseResult.class);
                if (result.success()){
                    OperateIncome income = GsonUtil.jsonToObject(result.getResult(),OperateIncome.class);
                    List<OperateIncomeItem> list = GsonUtil.jsonToList(income.balance_info,OperateIncomeItem.class);
                    view.getInfoSuccess(income,list);
                }
            }
        });
    }
}