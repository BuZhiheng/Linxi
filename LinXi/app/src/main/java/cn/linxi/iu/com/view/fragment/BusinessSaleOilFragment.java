package cn.linxi.iu.com.view.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.adapter.StationPriceAdapter;
import cn.linxi.iu.com.model.StationOilType;
import cn.linxi.iu.com.presenter.BusinessSaleOilPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessSaleOilPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IBusinessSaleOilView;
import cn.linxi.iu.com.view.widget.CarCardSelectPopupWindow;
import cn.linxi.iu.com.view.widget.ProvinceSelectPopupWindow;
/**
 * Created by buzhiheng on 2017/4/25.
 */
public class BusinessSaleOilFragment extends Fragment implements IBusinessSaleOilView, View.OnClickListener{
    private IBusinessSaleOilPresenter presenter;
    private View view;
    @Bind(R.id.rv_business_sale_station_price)
    RecyclerView rvPrice;
    @Bind(R.id.tv_business_sale_selectp)
    TextView tvProvince;
    @Bind(R.id.et_business_saleby_plate)
    EditText etPlate;
    private StationPriceAdapter adapter;
    private PopupWindow popProvince;
    private PopupWindow popCarCard;
    private String province = "鲁";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_business_sale_oil,container,false);
        }
        presenter = new BusinessSaleOilPresenter(this);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }
    private void initView() {
        popProvince = new ProvinceSelectPopupWindow((AppCompatActivity) getActivity());
        popCarCard = new CarCardSelectPopupWindow((AppCompatActivity) getActivity());
        adapter = new StationPriceAdapter(getContext(), new StationPriceAdapter.OnItemClick() {
            @Override
            public void click(int i, StationOilType price) {
                adapter.initItems(i);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPrice.setLayoutManager(manager);
        rvPrice.setAdapter(adapter);
        tvProvince.setOnClickListener(this);
        etPlate.setOnClickListener(this);
        presenter.getStationDetail((AppCompatActivity) getActivity());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_business_sale_selectp:
                popProvince.showAtLocation(view.findViewById(R.id.ll_business_sale), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.et_business_saleby_plate:
                popCarCard.showAtLocation(view.findViewById(R.id.ll_business_sale), Gravity.BOTTOM, 0, 0);
                break;
        }
    }
    public void onProvinceClick(View v){
        popProvince.dismiss();
        province = ((TextView) v).getText().toString();
        tvProvince.setText(province);
        popCarCard.showAtLocation(view.findViewById(R.id.ll_business_sale), Gravity.BOTTOM, 0, 0);
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
    @Override
    public void showToast(String noticeNetworkDisconnect) {
        ToastUtil.show(noticeNetworkDisconnect);
    }
    @Override
    public void setOilPriceWidth(int width) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rvPrice.getLayoutParams();
        params.width = width;
        rvPrice.setLayoutParams(params);
    }
    @Override
    public void setOilModel(List<StationOilType> priceList) {
        adapter.setData(priceList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setCardPlate(String p) {
        etPlate.setText(p);
    }
    @Override
    public void dismissCardPop() {
        popCarCard.dismiss();
    }
}