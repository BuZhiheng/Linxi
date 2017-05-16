package cn.linxi.iu.com.view.activity;
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
import org.xutils.x;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.Order;
import cn.linxi.iu.com.model.SaleOilCard;
import cn.linxi.iu.com.model.StationOilType;
import cn.linxi.iu.com.model.TransferBuyCaculate;
import cn.linxi.iu.com.presenter.TransferBuyPresenter;
import cn.linxi.iu.com.presenter.ipresenter.ITransferBuyPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.ITransferBuyView;
/**
 * Created by buzhiheng on 2017/5/8.
 */
public class TransferBuyActivity extends AppCompatActivity implements ITransferBuyView, View.OnClickListener{
    private ITransferBuyPresenter presenter;
    @Bind(R.id.tv_transfer_market_name)
    TextView tvName;
    @Bind(R.id.tv_transfer_market_address)
    TextView tvAddress;
    @Bind(R.id.tv_transfer_market_max)
    TextView tvMax;
    @Bind(R.id.tv_transfer_market_should_pay)
    TextView tvShouldPay;
    @Bind(R.id.tv_transfer_market_final_pay)
    TextView tvFinalPay;
    @Bind(R.id.tv_transfer_market_price)
    TextView tvPrice;
    @Bind(R.id.tv_transfer_market_save)
    TextView tvSave;
    @Bind(R.id.iv_transfer_market_photo)
    ImageView photo;
    @Bind(R.id.et_transfer_market_cout)
    EditText editText;
    @Bind(R.id.ll_transfer_market_item)
    LinearLayout layout;
    private StationOilType selecedPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_buy);
        ButterKnife.bind(this);
        presenter = new TransferBuyPresenter(this,getIntent());
        initView();
    }
    private void initView() {
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("转让市场");
        ((ImageView)findViewById(R.id.iv_titlebar_right)).setImageResource(R.drawable.ic_shopping_car);
        presenter.getData();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                presenter.getTransferMoney(editText,selecedPrice);
            }
        });
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void setData(SaleOilCard sale) {
        tvName.setText(sale.name);
        tvAddress.setText(sale.address);
        x.image().bind(photo, sale.avatar);
    }
    @Override
    public void addOilType(final StationOilType stationOilType, int i) {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_station_oillist, null);
        final TextView tv = (TextView) view.findViewById(R.id.tv_station_oil_name);
        tv.setText(stationOilType.oil_type);
        final int finalI = i;
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initOilModelList(finalI);
                //code 设置当前选中的price
                selecedPrice = stationOilType;
                tvMax.setText("最多"+selecedPrice.max_purchase+"L");
                presenter.getTransferMoney(editText,selecedPrice);
            }
        });
        if (i == 0){
            tv.setTextColor(ContextCompat.getColor(this, R.color.color_white));
            tv.setBackgroundResource(R.drawable.bg_ll_station_goods_yellow);
            selecedPrice = stationOilType;
            tvMax.setText("最多"+selecedPrice.max_purchase+"L");
            presenter.getTransferMoney(editText,selecedPrice);
        }
        layout.addView(view);
    }
    @Override
    public void setCaculate(TransferBuyCaculate caculate) {
        tvShouldPay.setText(caculate.station_amount);
        tvPrice.setText(caculate.price);
        tvSave.setText(caculate.saving_amount);
        tvFinalPay.setText(caculate.amount);
    }
    @Override
    public void orderSuccess(Order order) {
        showToast(order.oid);
    }
    private void initOilModelList(int curr) {
        int count = layout.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = layout.getChildAt(i);
            TextView tv = (TextView) view.findViewById(R.id.tv_station_oil_name);
            if (i == curr){
                tv.setTextColor(ContextCompat.getColor(this, R.color.color_white));
                tv.setBackgroundResource(R.drawable.bg_ll_station_goods_yellow);
            } else {
                tv.setTextColor(ContextCompat.getColor(this, R.color.color_black_text));
                tv.setBackgroundResource(R.drawable.bg_ll_station_goods_gray);
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.iv_transfer_market_cout_add:
                editText.setText(presenter.onCoutAdd(editText,selecedPrice));
                break;
            case R.id.iv_transfer_market_cout_sub:
                editText.setText(presenter.onCoutSub(editText,selecedPrice));
                break;
            case R.id.btn_transfer_buy_gothere:
                break;
            case R.id.btn_transfer_market_buy:
                presenter.order(editText,selecedPrice);
                break;
        }
    }
}