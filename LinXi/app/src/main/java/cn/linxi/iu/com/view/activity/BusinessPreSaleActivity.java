package cn.linxi.iu.com.view.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.presenter.BusinessPreSalePresenter;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessPreSalePresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IBusinessPreSaleView;
import cn.linxi.iu.com.view.widget.CarCardSelectPopupWindow;
import cn.linxi.iu.com.view.widget.ProvinceSelectPopupWindow;
/**
 * Created by buzhiheng on 2016/10/25.
 */
public class BusinessPreSaleActivity extends AppCompatActivity implements IBusinessPreSaleView {
    private IBusinessPreSalePresenter presenter;
    @Bind(R.id.ll_presale_byphone)
    LinearLayout llPhone;
    @Bind(R.id.ll_presale_byplate)
    LinearLayout llPlate;
    @Bind(R.id.tv_presale_byphone)
    TextView tvPhone;
    @Bind(R.id.tv_presale_byplate)
    TextView tvPlate;
    @Bind(R.id.tv_business_presale_selectp)
    TextView tvProvince;
    @Bind(R.id.et_business_salebyphone_phone)
    EditText etPhone;
    @Bind(R.id.et_business_salebyphone_code)
    EditText etCode;
    @Bind(R.id.btn_business_salebyphone_getcode)
    Button btnGetCode;
    @Bind(R.id.et_business_saleby_plate)
    EditText etPlate;
    private String time;
    private PopupWindow popProvince;
    private PopupWindow popCarCard;
    private String province = "鲁";
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x001:
                    btnGetCode.setText(time+"秒后重新获取");
                    break;
                case 0x002:
                    btnGetCode.setText("重新发送");
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_presale_next);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        presenter = new BusinessPreSalePresenter(this);
        popProvince = new ProvinceSelectPopupWindow(this);
        popCarCard = new CarCardSelectPopupWindow(this);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.fl_presale_back:
                finish();
                break;
            case R.id.tv_presale_byphone:
                setPhone();
                break;
            case R.id.tv_presale_byplate:
                setPlate();
                break;
            case R.id.btn_business_salebyphone_getcode:
                presenter.getCode(etPhone);
                break;
            case R.id.btn_business_salebyphone_commit:
                presenter.saleCheckCode(etPhone, etCode);
                break;
            case R.id.btn_business_salebyplate_commit:
                presenter.sale(province, etPlate);
                break;
            case R.id.iv_business_presale_selectp:
            case R.id.tv_business_presale_selectp:
                popProvince.showAtLocation(findViewById(R.id.fl_presale), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.et_business_saleby_plate:
                popCarCard.showAtLocation(findViewById(R.id.fl_presale), Gravity.BOTTOM, 0, 0);
                break;
        }
    }
    public void onProvinceClick(View v){
        popProvince.dismiss();
        province = ((TextView) v).getText().toString();
        tvProvince.setText(province);
        popCarCard.showAtLocation(findViewById(R.id.fl_presale), Gravity.BOTTOM, 0, 0);
    }
    public void onCarCardClick(View v){
        presenter.onCarCardPopClick(etPlate, ((TextView) v).getText().toString());
    }
    public void onProvinceCancel(View v){
        popProvince.dismiss();
    }
    public void subCarCard(View v){
        presenter.onSubCarCardClick(etPlate);
    }
    private void setPhone(){
        tvPhone.setTextColor(ContextCompat.getColor(this, R.color.color_white));
        tvPlate.setTextColor(ContextCompat.getColor(this, R.color.color_gray));
        llPlate.setVisibility(View.GONE);
        llPhone.setVisibility(View.VISIBLE);
    }
    private void setPlate(){
        tvPhone.setTextColor(ContextCompat.getColor(this, R.color.color_gray));
        tvPlate.setTextColor(ContextCompat.getColor(this, R.color.color_white));
        llPlate.setVisibility(View.VISIBLE);
        llPhone.setVisibility(View.GONE);
    }
    @Override
    public void refreshCodeButton(String time) {
        this.time = time;
        handler.sendEmptyMessage(0x001);
    }
    @Override
    public void setCodeBtnCanClick() {
        btnGetCode.setClickable(true);
        handler.sendEmptyMessage(0x002);
    }
    @Override
    public void setCodeBtnCanNotClick() {
        btnGetCode.setClickable(false);
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void toPreOilView(String identify) {
        Intent intent = new Intent(this,BusinessPreSaleOilActivity.class);
        intent.putExtra(CommonCode.INTENT_COMMON,identify);
        startActivity(intent);
        finish();
    }
    @Override
    public void dismissCardPop() {
        popCarCard.dismiss();
    }
    @Override
    public void setCardPlate(String plate) {
        etPlate.setText(plate);
    }
}