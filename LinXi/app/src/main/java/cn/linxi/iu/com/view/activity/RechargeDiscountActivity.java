package cn.linxi.iu.com.view.activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.adapter.RechargeDiscountAdapter;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.EventUserMsgChanged;
import cn.linxi.iu.com.model.EventWxPaySuccess;
import cn.linxi.iu.com.model.RechargeDiscount;
import cn.linxi.iu.com.presenter.RechargeDiscountPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IRechargeDiscountPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IRechargeDiscountView;
/**
 * Created by buzhiheng on 2017/3/8.
 */
public class RechargeDiscountActivity extends AppCompatActivity implements IRechargeDiscountView, View.OnClickListener{
    private IRechargeDiscountPresenter presenter;
    @Bind(R.id.rv_recharge_discount)
    RecyclerView rvDiscount;
    private RechargeDiscountAdapter adapter;
    @Bind(R.id.iv_recharge_checkzfb)
    ImageView ivCheckAli;
    @Bind(R.id.iv_recharge_checkwx)
    ImageView ivCheckWX;
    @Bind(R.id.btn_recharge_discount)
    Button btnPay;
    private int payType = CommonCode.PAY_BY_ZFB;
    private Map<String,String> result;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String resultStatus = result.get("resultStatus");
            String memo = result.get("memo");
            if ("9000".equals(resultStatus)){
                EventBus.getDefault().post(new EventUserMsgChanged());
                showToast("支付成功");
                finish();
            } else {
                showToast(memo);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_discount);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }
    private void initView() {
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("储值卡");
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        adapter = new RechargeDiscountAdapter(this, new RechargeDiscountAdapter.ItemClickListener() {
            @Override
            public void onItemClick(RechargeDiscount discount) {
                presenter.onItemClick(discount);
                btnPay.setText("支付:"+discount.pay_ment+"元");
            }
        });
        rvDiscount.setLayoutManager(manager);
        rvDiscount.setAdapter(adapter);
        presenter = new RechargeDiscountPresenter(this,getIntent());
        presenter.getDiscount();
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void setDiscount(List<RechargeDiscount> list) {
        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void aliPayResult(Map<String, String> stringStringMap) {
        this.result = stringStringMap;
        handler.sendEmptyMessage(0x001);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventWxPaySuccess paySuccess){//支付成功
        //EventBus事件方法,充值成功finish
//        showToast("充值成功");
        finish();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.fl_recharge_zfb:
                payType = CommonCode.PAY_BY_ZFB;
                ivCheckAli.setImageResource(R.drawable.ic_station_checked);
                ivCheckWX.setImageResource(R.drawable.ic_station_check);
                break;
            case R.id.fl_recharge_wx:
                payType = CommonCode.PAY_BY_WX;
                ivCheckAli.setImageResource(R.drawable.ic_station_check);
                ivCheckWX.setImageResource(R.drawable.ic_station_checked);
                break;
            case R.id.btn_recharge_discount:
                presenter.pay(payType);
                break;
        }
    }
}