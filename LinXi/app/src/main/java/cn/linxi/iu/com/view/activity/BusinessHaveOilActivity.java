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
import cn.linxi.iu.com.model.BusinessOrderCreated;
import cn.linxi.iu.com.model.CustomerOilCard;
import cn.linxi.iu.com.presenter.BusinessHaveOilPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessHaveOilPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IBusinessHaveOilView;
/**
 * Created by buzhiheng on 2016/8/31.
 */
public class BusinessHaveOilActivity extends AppCompatActivity implements IBusinessHaveOilView{
    private IBusinessHaveOilPresenter presenter;
    @Bind(R.id.ll_business_haveoil)
    LinearLayout llOilList;
    @Bind(R.id.tv_business_haveoil)
    TextView tvHaveOil;
    @Bind(R.id.tv_business_haveoil_purtype)
    TextView tvPurType;
    @Bind(R.id.et_business_haveoil_purchase)
    EditText etPurchase;
    private CustomerOilCard selectCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_haveoil);
        ButterKnife.bind(this);
        presenter = new BusinessHaveOilPresenter(this);
        initView();
    }
    private void initView() {
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("剩余油量");
        presenter.getCustomerMsg(getIntent());
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
            case R.id.btn_business_haveoil_cancel:
                finish();
                break;
            case R.id.btn_business_haveoil_sure:
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
    public void getCustomerMsgSuccess(List<CustomerOilCard> list) {
        llOilList.removeAllViews();
        for (int i=0;i<list.size();i++){
            final CustomerOilCard card = list.get(i);
            View view = LayoutInflater.from(this).inflate(R.layout.activity_business_haveoil_item,null);
            final FrameLayout fl = (FrameLayout) view.findViewById(R.id.fl_business_haveoil_content);
            TextView tvPur = (TextView) view.findViewById(R.id.tv_business_haveoil_pur);
            TextView tvType = (TextView) view.findViewById(R.id.tv_business_haveoil_oiltype);
            tvPur.setText(card.purchase);
            tvType.setText(card.oil_type);
            final ImageView imageView = (ImageView) view.findViewById(R.id.iv_business_haveoil);
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
            if (i == 0){
                imageView.setImageResource(R.drawable.ic_station_checked);
                selectCard = card;
                setIvType(card.type);
            }
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
    public void orderCreatedSuccess(BusinessOrderCreated orderCreated) {
        showToast("订单创建成功");
        finish();
    }
    @Override
    public void customerHaveNoOilCard(String err) {
        tvHaveOil.setText(err);
        findViewById(R.id.btn_business_haveoil_sure).setVisibility(View.GONE);
        etPurchase.setVisibility(View.GONE);
    }
    @Override
    public void havanoOil() {
        showToast("该用户未购买办加油站产品");
        finish();
    }
}