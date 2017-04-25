package cn.linxi.iu.com.view.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.OperateMine;
import cn.linxi.iu.com.presenter.BusinessMinePresenter;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessMinePresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.activity.BusinessIncomeActivity;
import cn.linxi.iu.com.view.activity.BusinessShareQrcodeActivity;
import cn.linxi.iu.com.view.iview.IBusinessMineView;
/**
 * Created by buzhiheng on 2016/12/3.
 */
public class BusinessMineFragment extends Fragment implements IBusinessMineView, View.OnClickListener {
    private IBusinessMinePresenter presenter;
    private View view;
    @Bind(R.id.srl_business_minefrm)
    SwipeRefreshLayout refresh;
    @Bind(R.id.tv_business_mine_name)
    TextView tvName;
    @Bind(R.id.tv_business_mine_commandcode)
    TextView tvCode;
    @Bind(R.id.tv_business_mine_phone)
    TextView tvPhone;
    @Bind(R.id.tv_business_mine_command)
    TextView tvCommandCout;
    @Bind(R.id.tv_business_mine_income)
    TextView tvIncome;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null){
            return view;
        }
        view = inflater.inflate(R.layout.fragment_business_mine,container,false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }
    private void initView() {
        presenter = new BusinessMinePresenter(this);
        presenter.getUser();
        view.findViewById(R.id.iv_business_share).setOnClickListener(this);
//        view.findViewById(R.id.fl_business_mine_income).setOnClickListener(this);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getUser();
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_business_share:
                Intent intentShare = new Intent(getContext(),BusinessShareQrcodeActivity.class);
                startActivity(intentShare);
                break;
            case R.id.fl_business_mine_income:
                Intent intentInccome = new Intent(getContext(),BusinessIncomeActivity.class);
                startActivity(intentInccome);
                break;
        }
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
        refresh.setRefreshing(false);
    }
    @Override
    public void setUser(OperateMine mine) {
        tvName.setText(mine.realname);
        tvPhone.setText(mine.mobile);
//        tvCommandCout.setText(mine.promote_count);
//        tvIncome.setText(mine.balance);
        tvCode.setText("推广编号:"+mine.extension_number);
        refresh.setRefreshing(false);
    }
}