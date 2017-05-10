package cn.linxi.iu.com.view.activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.x;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.adapter.TransferSaleTagPriceAdapter;
import cn.linxi.iu.com.model.TransferSaleDetail;
import cn.linxi.iu.com.model.TransferSaleTagPrice;
import cn.linxi.iu.com.presenter.TransferSalePresenter;
import cn.linxi.iu.com.presenter.ipresenter.ITransferSalePresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.ITransferSaleView;
/**
 * Created by buzhiheng on 2017/5/9.
 */
public class TransferSaleActivity extends AppCompatActivity implements ITransferSaleView, View.OnClickListener{
    private ITransferSalePresenter presenter;
    @Bind(R.id.tv_transfer_sale_station)
    TextView tvStation;
    @Bind(R.id.tv_transfer_sale_price)
    TextView tvPrice;
    @Bind(R.id.tv_transfer_sale_address)
    TextView tvAddress;
    @Bind(R.id.tv_transfer_sale_oil)
    TextView tvOilType;
    @Bind(R.id.iv_transfer_sale_station)
    ImageView ivPhoto;
    @Bind(R.id.et_transfer_sale_price_cout)
    EditText etPrice;
    @Bind(R.id.et_transfer_sale_purchase_cout)
    EditText etPurchase;
    @Bind(R.id.rv_transfer_sale_price)
    RecyclerView rvPrice;
    private TransferSaleTagPriceAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_sale);
        ButterKnife.bind(this);
        presenter = new TransferSalePresenter(this,getIntent());
        initView();
    }
    private void initView() {
        ((TextView) findViewById(R.id.tv_titlebar_title)).setText("转让");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPrice.setLayoutManager(manager);
        adapter = new TransferSaleTagPriceAdapter(this);
        rvPrice.setAdapter(adapter);
        presenter.getData();
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.iv_transfer_sale_price_sub:
                etPrice.setText(presenter.onPriceSub(etPrice));
                break;
            case R.id.iv_transfer_sale_price_add:
                etPrice.setText(presenter.onPriceAdd(etPrice));
                break;
            case R.id.iv_transfer_sale_purchase_sub:
                etPurchase.setText(presenter.onCoutSub(etPurchase));
                break;
            case R.id.iv_transfer_sale_purchase_add:
                etPurchase.setText(presenter.onCoutAdd(etPurchase));
                break;
            case R.id.tv_transfer_sale_cancel:
                finish();
                break;
            case R.id.tv_transfer_sale_sure:
                presenter.sure(etPrice,etPurchase);
                break;
        }
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void setData(TransferSaleDetail detail) {
        tvStation.setText(detail.name);
        tvAddress.setText(detail.address);
        tvPrice.setText("挂牌价："+detail.price);
        etPrice.setText(detail.transfor_price);
        etPurchase.setText(detail.transfor_purchase);
        tvOilType.setText("油品："+detail.oil_type);
        x.image().bind(ivPhoto,detail.avatar);
    }
    @Override
    public void saleSuccess() {
        showToast("转让成功!");
        finish();
    }
    @Override
    public void setTagPrice(List<TransferSaleTagPrice> prices) {
        adapter.setData(prices);
        adapter.notifyDataSetChanged();
    }
}