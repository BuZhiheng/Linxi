package cn.linxi.iu.com.presenter;
import android.content.Intent;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.presenter.ipresenter.IOilDetailTypePresenter;
import cn.linxi.iu.com.view.iview.IOilDetailTypeView;
/**
 * Created by buzhiheng on 2017/3/7.
 */
public class OilDetailTypePresenter implements IOilDetailTypePresenter {
    public OilDetailTypePresenter(IOilDetailTypeView view,Intent intent){
        if (intent == null){
            return;
        }
        int type = intent.getIntExtra(CommonCode.INTENT_COMMON,CommonCode.STATION_TYPE_BOTH);
        if (type == CommonCode.STATION_TYPE_OIL){
            view.showOilFrm();
        } else if (type == CommonCode.STATION_TYPE_GOODS){
            view.showGoodsFrm();
        } else if (type == CommonCode.STATION_TYPE_BOTH){
            view.showBothFrm();
        }
    }
}