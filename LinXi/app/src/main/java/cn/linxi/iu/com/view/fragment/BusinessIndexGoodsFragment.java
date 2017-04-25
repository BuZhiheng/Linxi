package cn.linxi.iu.com.view.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bigkoo.pickerview.TimePickerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.adapter.BusinessIndexAdapter;
import cn.linxi.iu.com.model.BusinessIndexData;
import cn.linxi.iu.com.model.BusinessIndexDataItem;
import cn.linxi.iu.com.presenter.BusinessIndexGoodsFrmPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessIndexOilFrmPresenter;
import cn.linxi.iu.com.util.TimeUtil;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IBusinessIndexView;
/**
 * Created by buzhiheng on 2017/4/13.
 */
public class BusinessIndexGoodsFragment extends Fragment implements IBusinessIndexView, View.OnClickListener {
    private IBusinessIndexOilFrmPresenter presenter;
    private View view;
    @Bind(R.id.srl_business_indexfrm)
    SwipeRefreshLayout refresh;
    @Bind(R.id.tv_business_indexfrm_name)
    TextView tvName;
    @Bind(R.id.tv_business_indexfrm_code)
    TextView tvUserCode;
    @Bind(R.id.tv_business_index_oil_left)
    TextView tvLeft;
    @Bind(R.id.tv_business_index_oil_right)
    TextView tvRight;
    @Bind(R.id.ll_business_indexoil_start)
    LinearLayout llStart;
    @Bind(R.id.ll_business_indexoil_end)
    LinearLayout llEnd;
    @Bind(R.id.tv_business_index_start)
    TextView tvStart;
    @Bind(R.id.tv_business_indexoil_end)
    TextView tvEnd;
    @Bind(R.id.tv_business_index_start_week)
    TextView tvStartWeek;
    @Bind(R.id.tv_business_index_end_week)
    TextView tvEndWeek;
    @Bind(R.id.rv_business_index)
    RecyclerView rvIndex;
    private BusinessIndexAdapter adapter;
    private TimePickerView timePStart;
    private TimePickerView timePEnd;
    private String timeStart = "";
    private String timeEnd = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null){
            return view;
        }
        view = inflater.inflate(R.layout.fragment_business_index_goods,container,false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }
    private void initView() {
        llStart.setOnClickListener(this);
        llEnd.setOnClickListener(this);
        timePStart = new TimePickerView(getContext(),TimePickerView.Type.YEAR_MONTH_DAY);
        timePStart.setCancelable(true);
        timePStart.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                tvStartWeek.setText(TimeUtil.getWeek(date));
                SimpleDateFormat format = new SimpleDateFormat(TimeUtil.FORMAT_YYMMDD);
                timeStart = format.format(date);
                SimpleDateFormat format1 = new SimpleDateFormat(TimeUtil.FORMAT_MMDD);
                setStime(format1.format(date));
                presenter.getData(timeStart, timeEnd);
            }
        });
        timePEnd = new TimePickerView(getContext(),TimePickerView.Type.YEAR_MONTH_DAY);
        timePEnd.setCancelable(true);
        timePEnd.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                tvEndWeek.setText(TimeUtil.getWeek(date));

                SimpleDateFormat format = new SimpleDateFormat(TimeUtil.FORMAT_YYMMDD);
                timeEnd = format.format(date);
                SimpleDateFormat format1 = new SimpleDateFormat(TimeUtil.FORMAT_MMDD);
                setEtime(format1.format(date));
                presenter.getData(timeStart, timeEnd);
            }
        });
        adapter = new BusinessIndexAdapter(getActivity());
        rvIndex.setLayoutManager(new LinearLayoutManager(getContext()));
        rvIndex.setAdapter(adapter);
        presenter = new BusinessIndexGoodsFrmPresenter(this);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getData(timeStart, timeEnd);
            }
        });
        tvStartWeek.setText(TimeUtil.getWeek(new Date()));
        tvEndWeek.setText(TimeUtil.getWeek(new Date()));
    }
    public void setStime(String stime){
        tvStart.setText(stime);
    }
    public void setEtime(String etime){
        tvEnd.setText(etime);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_business_indexoil_start:
                timePStart.show();
                break;
            case R.id.ll_business_indexoil_end:
                timePEnd.show();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getData(timeStart,timeEnd);
    }

    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void setData(BusinessIndexData data) {
        tvName.setText(data.username);
        tvUserCode.setText("推广编号"+data.extension_number);
        tvLeft.setText(data.left_button);
        tvRight.setText(data.right_button);
        refresh.setRefreshing(false);
    }
    @Override
    public void setDataItem(List<BusinessIndexDataItem> list) {
        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }
}