package cn.linxi.iu.com.view.activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.StationOilType;
import cn.linxi.iu.com.presenter.BusinessPreSaleOilPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessPreSaleOilPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IBusinessPreSaleOilView;
import cn.linxi.iu.com.view.widget.BusinessProsaleDialog;
import cn.linxi.iu.com.view.widget.MyDialog;
/**
 * Created by buzhiheng on 2016/10/25.
 * Desc 油品预售页面
 */
public class BusinessPreSaleOilActivity extends AppCompatActivity implements IBusinessPreSaleOilView {
    private IBusinessPreSaleOilPresenter presenter;
    @Bind(R.id.ll_presale_oiltype)
    LinearLayout llOilType;
    @Bind(R.id.et_business_presale_purchase)
    EditText etPurchase;
    @Bind(R.id.tv_business_presale_price)
    TextView tvPrice;
    @Bind(R.id.tv_business_presale_amount)
    TextView tvAmount;
    @Bind(R.id.iv_business_presale_clear)
    ImageView ivClear;
    private StationOilType selecedPrice;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_presale_oil);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        presenter = new BusinessPreSaleOilPresenter(this,getIntent());
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("油品预售");
        presenter.getOilType();
        etPurchase.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                presenter.getPriceAndCount(selecedPrice,etPurchase);
                if (etPurchase.getText().length()>0){
                    ivClear.setVisibility(View.VISIBLE);
                } else {
                    ivClear.setVisibility(View.GONE);
                }
            }
        });
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.btn_presale_cancel:
                finish();
                break;
            case R.id.btn_presale_commit:
                presenter.checkPreSale(etPurchase, selecedPrice);
                break;
            case R.id.iv_business_presale_clear:
                etPurchase.setText("");
                ivClear.setVisibility(View.GONE);
                break;
        }
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void setOilType(List<StationOilType> list) {
        llOilType.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            final StationOilType detail = list.get(i);
            View view = LayoutInflater.from(this).inflate(R.layout.activity_business_presale_oillist, null);
            final TextView tv = (TextView) view.findViewById(R.id.tv_station_oil_name);
            tv.setText(detail.oil_type);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initOilModelList();
                    //code 设置当前选中的price
                    selecedPrice = detail;
                    tv.setTextColor(ContextCompat.getColor(BusinessPreSaleOilActivity.this, R.color.color_white));
                    tv.setBackgroundResource(R.drawable.bg_ll_station_goods_yellow);
                    presenter.getPriceAndCount(selecedPrice,etPurchase);
                }
            });
            if (i == 0){
                tv.setTextColor(ContextCompat.getColor(BusinessPreSaleOilActivity.this, R.color.color_white));
                tv.setBackgroundResource(R.drawable.bg_ll_station_goods_yellow);
                selecedPrice = detail;
                presenter.getPriceAndCount(selecedPrice,etPurchase);
            }
            llOilType.addView(view);
        }
    }
    @Override
    public void saleSuccess() {
        showToast("预售成功");
        finish();
    }
    @Override
    public void setPrice(String price, String amount) {
        tvPrice.setText(price);
        tvAmount.setText(amount);
    }
    @Override
    public void showSureDialog(String id,String money) {
        dialog = new BusinessProsaleDialog(this,id,money,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.toPreSale(etPurchase,selecedPrice);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void initOilModelList() {
        int count = llOilType.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = llOilType.getChildAt(i);
            TextView tv = (TextView) view.findViewById(R.id.tv_station_oil_name);
            tv.setTextColor(ContextCompat.getColor(this, R.color.color_black_text));
            tv.setBackgroundResource(R.drawable.bg_ll_station_goods_gray);
        }
    }
}