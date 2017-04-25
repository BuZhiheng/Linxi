package cn.linxi.iu.com.view.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.adapter.SelectCityAdapter;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.SelectCity;
import cn.linxi.iu.com.presenter.SelectCityPresenter;
import cn.linxi.iu.com.presenter.ipresenter.ISelectCityPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.ISelectCityView;
import cn.linxi.iu.com.view.widget.OnRvScrollListener;
/**
 * Created by buzhiheng on 2016/7/29.
 */
public class SelectCityActivity extends AppCompatActivity implements ISelectCityView{
    private ISelectCityPresenter presenter;
    @Bind(R.id.rv_select_city)
    RecyclerView rvCity;
    @Bind(R.id.srl_select_city)
    SwipeRefreshLayout refresh;
    @Bind(R.id.tv_selectcity_current)
    TextView tvCrrentCity;
    private SelectCityAdapter adapter;
    private int page = 1;
    private SelectCity currentCity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        presenter = new SelectCityPresenter(this,this);
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("选择城市");
        refresh.setProgressViewOffset(false, CommonCode.OFFSET_START, CommonCode.OFFSET_END);
        refresh.setRefreshing(true);
        rvCity.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SelectCityAdapter(this, new SelectCityAdapter.ItemClickListener() {
            @Override
            public void click(SelectCity city) {
                Intent intent = new Intent();
                intent.putExtra(CommonCode.INTENT_REGISTER_USER,city);
                setResult(CommonCode.ACTIVITY_RESULT_CODE_BUY,intent);
                finish();
            }
        });
        rvCity.setAdapter(adapter);
        rvCity.addOnScrollListener(new OnRvScrollListener() {
            @Override
            public void toBottom() {
                super.toBottom();
                page++;
                presenter.getCityList(page);
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                presenter.getCityList(page);
            }
        });
        presenter.getCityList(page);
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
            case R.id.tv_selectcity_current:
                Intent intent = new Intent();
                intent.putExtra(CommonCode.INTENT_REGISTER_USER,currentCity);
                setResult(CommonCode.ACTIVITY_RESULT_CODE_BUY,intent);
                finish();
                break;
        }
    }
    @Override
    public void showToast(String toast) {
        refresh.setRefreshing(false);
        ToastUtil.show(toast);
    }
    @Override
    public void setCurrentCity(SelectCity currentCity,String city) {
        this.currentCity = currentCity;
        tvCrrentCity.setText(city);
    }
    @Override
    public void getCitySuccess(List<SelectCity> list) {
        if (refresh.isRefreshing()){
            adapter.setData(list);
        } else {
            adapter.addData(list);
        }
        adapter.notifyDataSetChanged();
        refresh.setRefreshing(false);
    }
}