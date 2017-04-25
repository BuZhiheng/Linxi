package cn.linxi.iu.com.presenter.ipresenter;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.linxi.iu.com.presenter.BusinessAfterScanPresenter;
/**
 * Created by buzhiheng on 2017/4/14.
 */
public interface IBusinessAfterScanPresenter {
    void getData(Intent intent);
    void orderSure(LinearLayout llGoods, LinearLayout llGoodsCout);
    void setGoodsCheck(ImageView imageView, BusinessAfterScanPresenter.OnGoodsCheckListener onGoodsCheckListener);
    void order();
}