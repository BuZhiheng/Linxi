package cn.linxi.iu.com.view.fragment;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.xutils.x;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.adapter.StationPriceAdapter;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.OrderDetail;
import cn.linxi.iu.com.model.Rebate;
import cn.linxi.iu.com.model.Station;
import cn.linxi.iu.com.model.StationOilType;
import cn.linxi.iu.com.presenter.StationDetailOilTypePresenter;
import cn.linxi.iu.com.presenter.ipresenter.IStationDetailPresenter;
import cn.linxi.iu.com.util.BitmapUtil;
import cn.linxi.iu.com.util.NAVIUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.activity.OrderActivity;
import cn.linxi.iu.com.view.activity.RechargeDiscountActivity;
import cn.linxi.iu.com.view.iview.IStationDetatilView;
import cn.linxi.iu.com.view.widget.MyDialog;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
/**
 * Created by buzhiheng on 2016/10/10.
 *
 */
public class StationOilTypeFragment extends Fragment implements IStationDetatilView ,View.OnClickListener {
    private View view;
    private IStationDetailPresenter presenter;
    @Bind(R.id.rv_station_price)
    RecyclerView rvPrice;
    @Bind(R.id.ll_station_oil_list)
    LinearLayout llOilList;
    @Bind(R.id.ll_station_detail_select_type)
    LinearLayout llSelect;
    @Bind(R.id.iv_stationdetail_photo)
    ImageView ivPhoto;
    @Bind(R.id.iv_stationdetail_clear)
    ImageView ivClear;
    @Bind(R.id.tv_stationdetail_name)
    TextView tvName;
    @Bind(R.id.tv_stationdetail_address)
    TextView tvAddress;
    @Bind(R.id.tv_stationdetail_tel)
    TextView tvTel;
    @Bind(R.id.et_stationdetail_oilcout)
    EditText etOil;
    @Bind(R.id.tv_station_purtype)
    TextView tvType;
    private String tel;
    private StationPriceAdapter adapter;
    private StationOilType selecedPrice;
    private Dialog dialog;
    private Station station;
    private String permissionCall = Manifest.permission.CALL_PHONE;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null){
            return view;
        }
        view = inflater.inflate(R.layout.fragment_station_detail_oiltype,container,false);
        presenter = new StationDetailOilTypePresenter(this, (AppCompatActivity) getActivity());
        ButterKnife.bind(this,view);
        initView();
        return view;
    }
    private void initView() {
        adapter = new StationPriceAdapter(getContext(), new StationPriceAdapter.OnItemClick() {
            @Override
            public void click(int i,StationOilType price) {
                initOilModelList(i);
                selecedPrice = price;
            }
        });
        dialog = MyDialog.getNoticeDialog(getContext(), "请稍后...");
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPrice.setLayoutManager(manager);
        rvPrice.setAdapter(adapter);
        presenter.getStationDetail();
        etOil.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (etOil.getText().length() > 0) {
                    ivClear.setVisibility(View.VISIBLE);
                } else {
                    ivClear.setVisibility(View.GONE);
                }
            }
        });
        view.findViewById(R.id.tv_automac_type_add).setOnClickListener(this);
        view.findViewById(R.id.tv_automac_type_buy).setOnClickListener(this);
        view.findViewById(R.id.tv_station_gonvg).setOnClickListener(this);
        view.findViewById(R.id.iv_stationdetail_clear).setOnClickListener(this);
        view.findViewById(R.id.iv_stationdetail_add).setOnClickListener(this);
        view.findViewById(R.id.iv_stationdetail_sub).setOnClickListener(this);
        view.findViewById(R.id.fl_stationdetail_tel).setOnClickListener(this);
        view.findViewById(R.id.fl_station_detail_select_oil).setOnClickListener(this);
        view.findViewById(R.id.fl_station_detail_select_money).setOnClickListener(this);
        llSelect.setOnClickListener(this);
    }
    private void initOilModelList(int curr) {
        int count = llOilList.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = llOilList.getChildAt(i);
            TextView tv = (TextView) view.findViewById(R.id.tv_station_oil_name);
            if (i == curr){
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));
                tv.setBackgroundResource(R.drawable.bg_ll_station_goods_yellow);
            } else {
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.color_black_text));
                tv.setBackgroundResource(R.drawable.bg_ll_station_goods_gray);
            }
        }
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_automac_type_buy:
                dialog.show();
                presenter.commitOrder(selecedPrice, etOil.getText().toString());
                break;
            case R.id.tv_automac_type_add:
                dialog.show();
                presenter.addShoppingCar(selecedPrice, etOil);
                break;
            case R.id.tv_station_gonvg:
                toNvg();
                break;
            case R.id.iv_stationdetail_clear:
                etOil.setText("");
                ivClear.setVisibility(View.GONE);
                break;
            case R.id.iv_stationdetail_add:
                presenter.oilCoutAdd(etOil);
                break;
            case R.id.iv_stationdetail_sub:
                presenter.oilCoutSub(etOil);
                break;
            case R.id.fl_stationdetail_tel:
                if (tel == null) {
                    showToast("数据错误");
                    return;
                }
                new AlertDialog.Builder(getContext())
                        .setMessage("拨打电话?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                presenter.checkPermission(permissionCall,100);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
                break;
            case R.id.fl_station_detail_select_oil:
                onOilClick();
                break;
            case R.id.fl_station_detail_select_money:
                onMoneyClick();
                break;
            case R.id.ll_station_detail_select_type:
                onOilClick();
                break;
        }
    }
    public void showSelect(){
        llSelect.setVisibility(View.VISIBLE);
//        llSelect.getVisibility();
    }
    public void onOilClick(){
        llSelect.setVisibility(View.GONE);
    }
    public void onMoneyClick(){
        llSelect.setVisibility(View.GONE);
        Intent intent = getActivity().getIntent();
        intent.setClass(getContext(), RechargeDiscountActivity.class);
        startActivity(intent);
    }
    @Override
    public void showToast(String toast) {
        dialog.dismiss();
        ToastUtil.show(toast);
    }
    @Override
    public void setStation(Station station,List<Rebate> list) {
        this.station = station;
        selecedPrice = null;
        x.image().bind(ivPhoto, station.avatar, BitmapUtil.getOptionRadius(30));
        tvName.setText(station.name);
        tvAddress.setText("  " + station.address);
        tvTel.setText(station.tel);
        tel = station.tel;
//        scrollView.fullScroll(ScrollView.FOCUS_UP);
    }
    @Override
    public void setOilModel(List<StationOilType> list) {
        llOilList.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            final StationOilType detail = list.get(i);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_station_oillist, null);
            final TextView tv = (TextView) view.findViewById(R.id.tv_station_oil_name);
            tv.setText(detail.oil_type);
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initOilModelList(finalI);
                    adapter.initItems(finalI);
                    //code 设置当前选中的price
                    selecedPrice = detail;
                    setIvType(detail.type);
                    rvPrice.smoothScrollToPosition(finalI);
                }
            });
            if (i == 0){
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));
                tv.setBackgroundResource(R.drawable.bg_ll_station_goods_yellow);
                selecedPrice = detail;
                setIvType(detail.type);
                adapter.initItems(0);
            }
            llOilList.addView(view);
        }
        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void setOilPriceWidth(int width) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rvPrice.getLayoutParams();
        params.width = width;
        rvPrice.setLayoutParams(params);
    }
    private void setIvType(String type){
        if ("1".equals(type)){
            tvType.setText("L");
        } else {
            tvType.setText("m³");
        }
    }
    @Override
    public void commitOrderSuccess(OrderDetail order) {
        dialog.dismiss();
        Intent intentBuy = new Intent(getContext(), OrderActivity.class);
        intentBuy.putExtra(CommonCode.INTENT_ORDER_ID, order.oid);
        startActivity(intentBuy);
        getActivity().finish();
    }
    @Override
    public void setOilCout(String cout) {
        etOil.setText(cout);
    }
    public void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        }
    }
    public void toNvg(){
        if (station == null || StringUtil.isNull(station.latitude + "")){
            showToast("数据获取失败");
        } else {
            NAVIUtil.toNAVIActivity(getContext(), station.latitude, station.longitude);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
    @PermissionSuccess(requestCode = 100)
    public void doCall(){
        callPhone();
    }
    @PermissionSuccess(requestCode = 200)
    public void doNvg(){
        toNvg();
    }
    @PermissionFail(requestCode = 100)
    public void doFailSomething(){
//        showToast("授权失败");
    }
}