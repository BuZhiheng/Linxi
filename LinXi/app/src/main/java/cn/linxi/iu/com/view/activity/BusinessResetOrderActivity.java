package cn.linxi.iu.com.view.activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.BusinessResetOrder;
import cn.linxi.iu.com.model.CustomerOilCard;
import cn.linxi.iu.com.presenter.BusinessResetOrderPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessResetOrderPresenter;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IBusinessResetOrderView;
/**
 * Created by buzhiheng on 2016/8/31.
 */
public class BusinessResetOrderActivity extends AppCompatActivity implements IBusinessResetOrderView{
    private IBusinessResetOrderPresenter presenter;
    @Bind(R.id.ll_business_resetorder)
    LinearLayout llOilList;
    @Bind(R.id.tv_business_resetorder)
    TextView tvHaveOil;
    @Bind(R.id.tv_business_haveoil_purtype)
    TextView tvPurType;
    @Bind(R.id.et_business_resetorder_purchase)
    EditText etPurchase;
    private CustomerOilCard selectCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_resetorder);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        presenter = new BusinessResetOrderPresenter(this);
        presenter.getCustomerMsg(getIntent());
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("修改订单");
    }
    private void initOilModelList(){
        int count = llOilList.getChildCount();
        for (int i=0;i<count;i++){
            View view = llOilList.getChildAt(i);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_business_haveoil);
            imageView.setImageResource(R.drawable.ic_station_check);
        }
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.btn_business_resetorder_cancel:
                finish();
                break;
            case R.id.btn_business_resetorder_sure:
                presenter.order(selectCard, etPurchase);
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void getCustomerOrderSuccess(BusinessResetOrder order,List<CustomerOilCard> list) {
        if (!StringUtil.isNull(order.purchase)){
            etPurchase.setText(order.purchase);
        }
        llOilList.removeAllViews();
        for (int i=0;i<list.size();i++){
            final CustomerOilCard card = list.get(i);
            View view = LayoutInflater.from(this).inflate(R.layout.activity_business_haveoil_item, null);
            final FrameLayout fl = (FrameLayout) view.findViewById(R.id.fl_business_haveoil_content);
            TextView tvPur = (TextView) view.findViewById(R.id.tv_business_haveoil_pur);
            TextView tvType = (TextView) view.findViewById(R.id.tv_business_haveoil_oiltype);
            tvPur.setText(card.purchase);
            tvType.setText(card.oil_type);
            final ImageView imageView = (ImageView) view.findViewById(R.id.iv_business_haveoil);
            if (order.details_id != null && order.details_id.equals(card.details_id)){
                imageView.setImageResource(R.drawable.ic_station_checked);
                selectCard = card;
                setIvType(card.type);
            }
            fl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initOilModelList();
                    //code 设置当前选中的price
                    selectCard = card;
                    imageView.setImageResource(R.drawable.ic_station_checked);
                    setIvType(card.type);
                }
            });
            llOilList.addView(view);
        }
    }
    private void setIvType(String type){
        if ("1".equals(type)){
            tvPurType.setText("L");
        } else {
            tvPurType.setText("m³");
        }
    }
    @Override
    public void orderCreatedSuccess() {
        showToast("订单修改成功");
        finish();
    }
    @Override
    public void customerHaveNoOilCard(String err) {
        tvHaveOil.setText(err);
        findViewById(R.id.btn_business_resetorder_sure).setClickable(false);
    }
}