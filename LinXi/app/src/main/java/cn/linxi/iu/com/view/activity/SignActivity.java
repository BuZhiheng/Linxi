package cn.linxi.iu.com.view.activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.adapter.SignAdapter;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.Sign;
import cn.linxi.iu.com.model.SignReward;
import cn.linxi.iu.com.presenter.SignPresenter;
import cn.linxi.iu.com.presenter.ipresenter.ISignPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.ISignView;
import cn.linxi.iu.com.view.widget.SignSuccessDialog;
/**
 * Created by buzhiheng on 2016/8/29.
 */
public class SignActivity extends AppCompatActivity implements ISignView {
    private ISignPresenter presenter;
    @Bind(R.id.rv_sign)
    RecyclerView rvSign;
    @Bind(R.id.srl_sign)
    SwipeRefreshLayout refresh;
    private SignAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        ButterKnife.bind(this);
        presenter = new SignPresenter(this);
        initView();
    }
    private void initView() {
        refresh.setProgressViewOffset(false, CommonCode.OFFSET_START, CommonCode.OFFSET_END);
        refresh.setRefreshing(true);
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("本月累计签到");
        adapter = new SignAdapter(this);
        GridLayoutManager manager = new GridLayoutManager(this,7);
        rvSign.setLayoutManager(manager);
        rvSign.setAdapter(adapter);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getSigned();
            }
        });
        presenter.getSigned();
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.btn_sign:
                presenter.sign();
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
        refresh.setRefreshing(false);
        ToastUtil.show(toast);
    }
    @Override
    public void setSignView(List<Sign> data) {
        adapter.setData(data);
        adapter.notifyDataSetChanged();
        refresh.setRefreshing(false);
    }
    @Override
    public void signSuccess(SignReward reward) {
        SignSuccessDialog dialog = new SignSuccessDialog(this,reward);
        dialog.show();
        presenter.getSigned();
    }
}