package cn.linxi.iu.com.view.activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.TransferOrder;
import cn.linxi.iu.com.model.TransferOrderDetail;
import cn.linxi.iu.com.presenter.TransferOrderPresenter;
import cn.linxi.iu.com.presenter.ipresenter.ITransferOrderPresenter;
import cn.linxi.iu.com.view.iview.ITransferOrderView;
/**
 * Created by buzhiheng on 2017/5/22.
 */
public class TransferOrderActivity extends AppCompatActivity implements ITransferOrderView, View.OnClickListener {
    private ITransferOrderPresenter presenter;
    @Bind(R.id.tv_transfer_order_type)
    TextView tvType;
    @Bind(R.id.tv_transfer_order_total)
    TextView tvTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_order);
        ButterKnife.bind(this);
        presenter = new TransferOrderPresenter(this,getIntent());
        initView();
    }
    private void initView() {
        ((TextView) findViewById(R.id.tv_titlebar_title)).setText("转让市场");
        presenter.getOrder();
    }
    @Override
    public void showToast(String toast) {
    }
    @Override
    public void setOrderData(TransferOrder order) {
        tvType.setText("  购买明细("+order.oil_type+")");
        tvTotal.setText("¥"+order.total);
    }
    @Override
    public void setOrderItem(TransferOrderDetail detail) {
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
        }
    }
}