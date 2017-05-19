package cn.linxi.iu.com.view.activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.Automac;
import cn.linxi.iu.com.model.SaleOilCard;
import cn.linxi.iu.com.model.UserHaveGoods;
import cn.linxi.iu.com.presenter.BusinessAfterScanPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessAfterScanPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IBusinessAfterScanView;
import cn.linxi.iu.com.view.widget.BusinessAfterScanSureDialog;
/**
 * Created by buzhiheng on 2017/4/13.
 */
public class BusinessAfterscanActivity extends AppCompatActivity implements IBusinessAfterScanView, View.OnClickListener{
    private IBusinessAfterScanPresenter presenter;
    @Bind(R.id.ll_business_afterscan_oilcontent)
    LinearLayout llOilContent;
    @Bind(R.id.ll_business_afterscan_goodscontent)
    LinearLayout llGoodsContent;
    @Bind(R.id.ll_business_afterscan_oil)
    LinearLayout llOil;
    @Bind(R.id.ll_business_afterscan_goods)
    LinearLayout llGoods;
    @Bind(R.id.ll_business_afterscan_goodscout)
    LinearLayout llGoodsCout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_afterscan);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        ((TextView) findViewById(R.id.tv_titlebar_title)).setText("扫一扫");
        presenter = new BusinessAfterScanPresenter(this);
        presenter.getData(getIntent());
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void setOilList(List<UserHaveGoods> list) {
        llOilContent.setVisibility(View.VISIBLE);
        for (int i=0;i<list.size();i++){
            TextView textView = new TextView(this);
            textView.setText(list.get(i).name);
            llOil.addView(textView);
        }
    }
    @Override
    public void setGoodsList(List<UserHaveGoods> listGoods) {
        llGoodsContent.setVisibility(View.VISIBLE);
        for (int i=0;i<listGoods.size();i++){
            final UserHaveGoods mac = listGoods.get(i);
            View view = LayoutInflater.from(this).inflate(R.layout.activity_business_afterscan_item, null);
            TextView tvName = (TextView) view.findViewById(R.id.tv_business_afterscan_name);
            TextView tvNum = (TextView) view.findViewById(R.id.tv_business_afterscan_num);
            final ImageView imageView = (ImageView) view.findViewById(R.id.iv_business_afterscan_check);
            tvName.setText(mac.name);
            tvNum.setText("x"+mac.num);
//            if (i == 0){
//                imageView.setImageResource(R.drawable.ic_station_checked);
//            }
            view.findViewById(R.id.fl_business_afterscan_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    initItem();
                    presenter.setGoodsCheck(imageView,new BusinessAfterScanPresenter.OnGoodsCheckListener() {
                        @Override
                        public void onClick(int drawable, String tag) {
                            imageView.setImageResource(drawable);
                            imageView.setTag(tag);
                        }
                    });
                }
            });
            View goodsCout = LayoutInflater.from(this).inflate(R.layout.activity_business_afterscan_goodscout,null);
            TextView tvNamec = (TextView) goodsCout.findViewById(R.id.tv_business_afterscan_name);
            tvNamec.setText(mac.name);
            llGoodsCout.addView(goodsCout);
            llGoods.addView(view);
        }
    }
    @Override
    public void orderSuccess() {
        finish();
    }
    @Override
    public void showUserBuyNothing() {
        showToast("该用户未购买本加油站任何商品");
        finish();
    }
//    private void initItem() {
//        for (int i=0;i<llGoods.getChildCount();i++){
//            View view = llGoods.getChildAt(i);
//            ImageView imageView = (ImageView) view.findViewById(R.id.iv_business_afterscan_check);
//            imageView.setImageResource(R.drawable.ic_station_check);
//        }
//    }
    public void showOrderSureDialog(List<Automac> list){
        Dialog dialog = new BusinessAfterScanSureDialog(this, list, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.order();
            }
        });
        dialog.show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fl_titlebar_back:
            case R.id.tv_business_afterscan_cancle:
                finish();
                break;
            case R.id.tv_business_afterscan_sure:
                presenter.orderSure(llGoods,llGoodsCout);
                break;
        }
    }
}