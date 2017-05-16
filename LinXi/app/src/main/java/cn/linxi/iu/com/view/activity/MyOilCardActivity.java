package cn.linxi.iu.com.view.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.adapter.MyOilCardAdapter;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.SaleOilCard;
import cn.linxi.iu.com.presenter.MyOilCardPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IMyOilCardPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IMyOilCardView;
import cn.linxi.iu.com.view.widget.OnRvScrollListener;
import cn.linxi.iu.com.view.widget.TransferDetailPopupWindow;
/**
 * Created by buzhiheng on 2016/8/4.
 * Desc 我的油卡页面
 */
public class MyOilCardActivity extends AppCompatActivity implements IMyOilCardView, View.OnClickListener {
    private IMyOilCardPresenter presenter;
    @Bind(R.id.rv_myoilcard)
    RecyclerView rvCard;
    @Bind(R.id.srl_myoilcard)
    SwipeRefreshLayout refresh;
    @Bind(R.id.ll_include_nodata)
    LinearLayout llNodata;
    @Bind(R.id.tv_titlebar_right)
    TextView tvRight;
    private MyOilCardAdapter adapter;
    private int page = 1;
    private TransferDetailPopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myoilcard);
        presenter = new MyOilCardPresenter(this);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        refresh.setProgressViewOffset(false, CommonCode.OFFSET_START, CommonCode.OFFSET_END);
        refresh.setRefreshing(true);
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("我的油卡");
        ((TextView)findViewById(R.id.tv_titlebar_right)).setText("油卡明细");
        adapter = new MyOilCardAdapter(this);
        rvCard.setLayoutManager(new LinearLayoutManager(this));
        rvCard.setAdapter(adapter);
        rvCard.addOnScrollListener(new OnRvScrollListener(){
            @Override
            public void toBottom() {
                super.toBottom();
//                page ++;
//                presenter.getMyOilCard(page);
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                presenter.getMyOilCard(page);
            }
        });
        presenter.getMyOilCard(page);
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
    public void onClick(View v){
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.tv_titlebar_right:
                popupWindow = new TransferDetailPopupWindow(this,this);
                popupWindow.showAsDropDown(tvRight, 0, 0);
                break;
            case R.id.tv_dialog_popwin_transfer:
                popupWindow.dismiss();
                break;
            case R.id.tv_dialog_popwin_transfer_oil:
                Intent intent = new Intent(this,OilDetailActivity.class);
                startActivity(intent);
                popupWindow.dismiss();
                break;
        }
    }
    @Override
    public void showToast(String toast) {
        refresh.setRefreshing(false);
        ToastUtil.show(toast);
    }

    @Override
    public void refreshRv(List<SaleOilCard> list) {
        llNodata.setVisibility(View.GONE);
        if (refresh.isRefreshing()){
            adapter.setData(list);
        } else {
            adapter.addData(list);
        }
        adapter.notifyDataSetChanged();
        refresh.setRefreshing(false);
    }
    @Override
    public void setNoData() {
        refresh.setRefreshing(false);
        llNodata.setVisibility(View.VISIBLE);
    }
}