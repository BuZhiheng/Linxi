package cn.linxi.iu.com.view.activity;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.SaleOilCardMsg;
import cn.linxi.iu.com.presenter.MyOilCardDetailPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IMyOilCardDetailPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IMyOilCardDetailView;
import cn.linxi.iu.com.view.widget.MyDialog;
import cn.linxi.iu.com.view.widget.PayPasswordView;
import cn.linxi.iu.com.view.widget.PayPsdDialog;
/**
 * Created by buzhiheng on 2016/8/9.
 */
public class MyOilCardDetailActivity extends Activity implements IMyOilCardDetailView{
    private IMyOilCardDetailPresenter presenter;
    @Bind(R.id.tv_mycard_detail_amount)
    TextView tvAmount;
    @Bind(R.id.tv_mycard_detail_original)
    TextView tvOra;
    @Bind(R.id.tv_mycard_detail_actual)
    TextView tvAct;
    private PayPsdDialog payPsdDialog;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myoilcard_detail);
        ButterKnife.bind(this);
        presenter = new MyOilCardDetailPresenter(this);
        presenter.getCardMsg(getIntent());
        dialog = MyDialog.getNoticeDialog(this, "请稍后...");
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_mycard_detail_cancel:
                finish();
                break;
            case R.id.btn_mycard_detail_sure:
                presenter.checkPsdBind();
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
        dialog.dismiss();
        ToastUtil.show(toast);
    }
    @Override
    public void setContent(SaleOilCardMsg card) {
        tvAmount.setText(card.total_amount+"");
        tvOra.setText(card.original_amount+"");
        tvAct.setText(card.actual_amount+"");
    }
    @Override
    public void showPayPsdDialog() {
        payPsdDialog = new PayPsdDialog(this, new PayPasswordView.OnPayListener() {
            @Override
            public void onCancelPay() {
                payPsdDialog.dismiss();
                setResult(CommonCode.ACTIVITY_RESULT_CODE_SALE);
                finish();
            }
            @Override
            public void onSurePay(String password) {
                presenter.sale(password);
                payPsdDialog.dismiss();
                dialog.show();
            }
            @Override
            public void onForgetPayPsd() {
                Intent intent = new Intent(MyOilCardDetailActivity.this,SafeCenterActivity.class);
                startActivity(intent);
            }
        });
        payPsdDialog.show();
    }
    @Override
    public void showNotBindPayPsd() {
        ToastUtil.show("您还没有设置支付密码");
        Intent intent = new Intent(this,ChangePayPsdActivity.class);
        startActivity(intent);
    }
    @Override
    public void showNotBindPhone() {
        ToastUtil.show("您还没有绑定手机");
        Intent intent = new Intent(this,BindPhoneActivity.class);
        startActivity(intent);
    }
    @Override
    public void saleSuccess() {
        showToast("卖出成功");
        setResult(CommonCode.ACTIVITY_RESULT_CODE_SALE);
        finish();
    }
}