package cn.linxi.iu.com.view.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.adapter.BuyFrmBannerViewHolder;
import cn.linxi.iu.com.adapter.PriceAdapter;
import cn.linxi.iu.com.adapter.StationAdapter;
import cn.linxi.iu.com.model.BuyFrmBanner;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.NormalPrice;
import cn.linxi.iu.com.model.SelectCity;
import cn.linxi.iu.com.model.Shared;
import cn.linxi.iu.com.model.Station;
import cn.linxi.iu.com.presenter.MainFrmPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IMainFrmPresenter;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.ShareManager;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.activity.FirstRegisterEnvelopActivity;
import cn.linxi.iu.com.view.activity.SelectCityActivity;
import cn.linxi.iu.com.view.activity.WebViewActivity;
import cn.linxi.iu.com.view.iview.IMainFrmView;
import cn.linxi.iu.com.view.widget.OnRvScrollListener;
import cn.linxi.iu.com.view.widget.SharePopupWindow;
/**
 * Created by buzhiheng on 2016/7/15.
 * Desc 买油主页
 */
public class BuyFragment extends Fragment implements IMainFrmView ,View.OnClickListener{
    private IMainFrmPresenter presenter;
    private View view;
    private final int HANDLE_TIMEOUT = 0X001;
    @Bind(R.id.tv_buyfrm_loccity)
    TextView tvCity;
    @Bind(R.id.tv_buyfrm_notice)
    TextView tvNotice;
    @Bind(R.id.ll_buyfrm_nonestation)
    LinearLayout llNotice;
    @Bind(R.id.banner_buyfrm)
    ConvenientBanner banner;
//    @Bind(R.id.rv_buyact_price)
//    RecyclerView rvPrice;
    @Bind(R.id.rv_buyact_station)
    RecyclerView rvStation;
//    PriceAdapter adapterPrice;
    @Bind(R.id.rv_buyact_header)
    RecyclerViewHeader header;
    @Bind(R.id.srl_buyfrm)
    SwipeRefreshLayout refresh;
    private SharePopupWindow popWin;
    private ShareManager manager;
    private Shared share;
    StationAdapter adapterStation;
    private String cityCode = "";
    int page = 1;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HANDLE_TIMEOUT:
//                    refresh.setRefreshing(false);
                    ToastUtil.show("请求超时");
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null){
            return view;
        }
        view = inflater.inflate(R.layout.fragment_buy,container,false);
        ButterKnife.bind(this,view);
        presenter = new MainFrmPresenter(this,getActivity());
        initView();
        return view;
    }
    private void initView() {
        refresh.setProgressViewOffset(false, CommonCode.OFFSET_START, CommonCode.OFFSET_END);
        refresh.setRefreshing(false);
        popWin = new SharePopupWindow((AppCompatActivity) getActivity(),this);
        manager = ShareManager.getInstance((AppCompatActivity) getActivity());
        view.findViewById(R.id.ll_buyfrm_loc).setOnClickListener(this);
        view.findViewById(R.id.ll_buyfrm_share).setOnClickListener(this);
        view.findViewById(R.id.fl_mainact_teach).setOnClickListener(this);
//        adapterPrice = new PriceAdapter(getContext());
        adapterStation = new StationAdapter(getContext());
//        LinearLayoutManager lm = new LinearLayoutManager(getContext());
//        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
//        rvPrice.setLayoutManager(lm);
//        rvPrice.setAdapter(adapterPrice);
        rvStation.setLayoutManager(new LinearLayoutManager(getContext()));
        rvStation.setAdapter(adapterStation);
        header.attachTo(rvStation);
        rvStation.addOnScrollListener(new OnRvScrollListener() {
            @Override
            public void toBottom() {
                super.toBottom();
                page++;
                presenter.getOilList(page, refresh, cityCode);
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                presenter.getOilList(page, refresh, cityCode);
//                presenter.getTodayPrice();
            }
        });
//        presenter.getOilList(page,refresh,cityCode);
//        presenter.getTodayPrice(cityCode);
        presenter.initBuy();
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ll_buyfrm_loc:
                Intent intentLoc = new Intent(getContext(), SelectCityActivity.class);
                startActivityForResult(intentLoc, CommonCode.ACTIVITY_RESULT_CODE_BUY);
                break;
            case R.id.fl_mainact_teach:
                Intent intentTeach = new Intent(getContext(), WebViewActivity.class);
                intentTeach.putExtra(CommonCode.INTENT_WEBVIEW_URL,CommonCode.APP_TEACH);
                startActivity(intentTeach);
                break;
            case R.id.ll_buyfrm_share:
                presenter.setShare();
                break;
            case R.id.ll_popwinshare_qq:
                manager.shareQQ(share);
                popWin.dismiss();
                break;
            case R.id.ll_popwinshare_qzone:
                share.setWxType(manager.SHARE_TYPE_SQUARE);
                manager.shareQQ(share);
                popWin.dismiss();
                break;
            case R.id.ll_popwinshare_wx:
                share.setWxType(manager.SHARE_TYPE_CHAT);
                manager.shareWx(share);
                popWin.dismiss();
                break;
            case R.id.ll_popwinshare_wxsquare:
                share.setWxType(manager.SHARE_TYPE_SQUARE);
                manager.shareWx(share);
                popWin.dismiss();
                break;
            case R.id.tv_popwinshare_cancel:
                popWin.dismiss();
                break;
        }
    }
    @Override
    public void showToast(String toast) {
        refresh.setRefreshing(false);
        ToastUtil.show(toast);
    }
    @Override
    public void removeStation() {
        adapterStation.removeData();
        adapterStation.notifyDataSetChanged();
        refresh.setRefreshing(false);
    }
    @Override
    public void addStationData(List<Station> list) {
        llNotice.setVisibility(View.GONE);
        if (refresh.isRefreshing()){
            adapterStation.setData(list);
        } else {
            adapterStation.addData(list);
        }
        adapterStation.notifyDataSetChanged();
        refresh.setRefreshing(false);
    }
    @Override
    public void addPriceData(List<NormalPrice> list) {
//        adapterPrice.setData(list);
//        adapterPrice.notifyDataSetChanged();
    }
    @Override
    public void setPriceWidth(int width) {
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rvPrice.getLayoutParams();
//        params.width = width;
//        rvPrice.setLayoutParams(params);
    }

    @Override
    public void setBanner(final List<BuyFrmBanner> banners) {
        banner.setPages(new CBViewHolderCreator<BuyFrmBannerViewHolder>() {
            @Override
            public BuyFrmBannerViewHolder createHolder() {
                return new BuyFrmBannerViewHolder(banners);
            }
        },banners).setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
        banner.startTurning(5000);
    }
    @Override
    public void setTimeOut() {
        handler.sendEmptyMessage(HANDLE_TIMEOUT);
    }
    @Override
    public void haveNoStation(String err) {
        llNotice.setVisibility(View.VISIBLE);
        tvNotice.setText(err);
    }
    @Override
    public void refreshBuy(SelectCity city) {
        onRefresh(city);
    }
    @Override
    public void setShare(Shared share) {
        this.share = share;
        popWin.showAtLocation(getActivity().findViewById(R.id.fl_main), Gravity.BOTTOM, 0, 0);
    }
    @Override
    public void toFirstLoginAct() {
        Intent intent = new Intent(getContext(), FirstRegisterEnvelopActivity.class);
        startActivity(intent);
    }
    public void initBuy(){
        presenter.initBuy();
    }
    public void onRefresh(SelectCity city){
        PrefUtil.putString(CommonCode.SP_LOC_CITY_LAST,city.city_name);
        PrefUtil.putString(CommonCode.SP_LOC_CITY_CODE_LAST,city.city_code+"");
//        ((TextView)view.findViewById(R.id.tv_buyfrm_loccity)).setText(city.city_name);
        tvCity.setText(city.city_name);
        cityCode = city.city_code+"";
        page = 1;
        refresh.setRefreshing(true);
        presenter.getOilList(page, refresh, cityCode);
        presenter.getTodayPrice(cityCode);
//        ToastUtil.show(city.city_name);
    }
}