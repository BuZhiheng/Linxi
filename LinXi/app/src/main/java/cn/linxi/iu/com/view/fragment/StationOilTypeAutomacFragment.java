package cn.linxi.iu.com.view.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.adapter.AutomacAdapter;
import cn.linxi.iu.com.adapter.AutomacBannerViewHolder;
import cn.linxi.iu.com.adapter.AutomacTypeAdapter;
import cn.linxi.iu.com.model.Automac;
import cn.linxi.iu.com.model.AutomacBanner;
import cn.linxi.iu.com.model.AutomacType;
import cn.linxi.iu.com.presenter.StationTypeAutomacPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IStationOilypeAutomacFrmPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IStationOilTypeAutomacFrmView;
/**
 * Created by buzhiheng on 2016/10/10.
 *
 */
public class StationOilTypeAutomacFragment extends Fragment implements IStationOilTypeAutomacFrmView, View.OnClickListener{
    private IStationOilypeAutomacFrmPresenter presenter;
    private View view;
    @Bind(R.id.banner_automac)
    ConvenientBanner banner;//
    @Bind(R.id.rv_automac_type)
    RecyclerView rvAutomacType;
    private AutomacTypeAdapter adapterAutomacType;
    @Bind(R.id.ll_automac_rv)
    LinearLayout llAutomacType;
    @Bind(R.id.rv_automac)
    RecyclerView rvAutomac;
    @Bind(R.id.tv_automactype_selectby_sale)
    TextView tvSelectSale;
    @Bind(R.id.tv_automactype_selectby_price)
    TextView tvSelectPrice;
    @Bind(R.id.iv_automactype_selectby_sale)
    ImageView ivSale;
    @Bind(R.id.iv_automactype_selectby_price)
    ImageView ivPrice;
    private AutomacAdapter adapterAutomac;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null){
            return view;
        }
        view = inflater.inflate(R.layout.fragment_station_detail_automac,container,false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }
    private void initView() {
        presenter = new StationTypeAutomacPresenter(this,getActivity().getIntent());
        //设置汽配类型横向滑动列表rv
        adapterAutomacType = new AutomacTypeAdapter(getContext(), new AutomacTypeAdapter.OnAutomacTypeClickListener() {
            @Override
            public void onTypeClick(AutomacType type) {
//                showToast(type.name);
                presenter.getAutoDataByPid(type.pid);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvAutomacType.setLayoutManager(manager);
        rvAutomacType.setAdapter(adapterAutomacType);
        //设置汽配列表rv
        adapterAutomac = new AutomacAdapter(getActivity());
        rvAutomac.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAutomac.setAdapter(adapterAutomac);
        //view初始化完毕,加载数据
        presenter.getAutoMacData();
        view.findViewById(R.id.ll_automactype_selectby_sale).setOnClickListener(this);
        view.findViewById(R.id.ll_automactype_selectby_price).setOnClickListener(this);
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void setAutomacType(List<AutomacType> list) {
        adapterAutomacType.setData(list);
        adapterAutomacType.notifyDataSetChanged();
    }
    @Override
    public void setAutomacData(List<Automac> list) {
        adapterAutomac.setData(list);
        adapterAutomac.notifyDataSetChanged();
    }
    @Override
    public void setAutomacBanner(final List<AutomacBanner> list) {
        //设置汽配广告banner
        banner.setPages(new CBViewHolderCreator<AutomacBannerViewHolder>() {
            @Override
            public AutomacBannerViewHolder createHolder() {
                return new AutomacBannerViewHolder(list);
            }
        },list).setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
        banner.startTurning(5000);
    }
    @Override
    public void setSortSale(int sort) {
        ivSale.setImageResource(sort);
    }
    @Override
    public void setSortPrice(int sort) {
        ivPrice.setImageResource(sort);
    }
    @Override
    public void setOilPriceWidth(int width) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llAutomacType.getLayoutParams();
        params.width = width;
        llAutomacType.setLayoutParams(params);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_automactype_selectby_sale:
                tvSelectSale.setTextColor(ContextCompat.getColor(getContext(),R.color.color_main));
                tvSelectPrice.setTextColor(ContextCompat.getColor(getContext(),R.color.color_black_text));
                presenter.getAutoDataBySale();
                break;
            case R.id.ll_automactype_selectby_price:
                tvSelectSale.setTextColor(ContextCompat.getColor(getContext(),R.color.color_black_text));
                tvSelectPrice.setTextColor(ContextCompat.getColor(getContext(),R.color.color_main));
                presenter.getAutoDataByPrice();
                break;
        }
    }
}