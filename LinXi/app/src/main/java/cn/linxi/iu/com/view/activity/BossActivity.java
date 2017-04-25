package cn.linxi.iu.com.view.activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.OnDismissListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.adapter.BossAdapter;
import cn.linxi.iu.com.model.BossMsgEmployeeDetail;
import cn.linxi.iu.com.model.BossTodayAmount;
import cn.linxi.iu.com.model.BossTodayPurchase;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.presenter.BossPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IBossPresenter;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IBossView;
import cn.linxi.iu.com.view.widget.MyDialog;
import cn.linxi.iu.com.view.widget.OnRvScrollListener;
/**
 * Created by buzhiheng on 2016/11/8.
 */
public class BossActivity extends AppCompatActivity implements IBossView{
    private IBossPresenter presenter;
    @Bind(R.id.iv_boss_calendar)
    ImageView ivSalendar;
    @Bind(R.id.tv_boss_calendar)
    TextView tvCalendar;

    @Bind(R.id.tv_boss_profit_amount)
    TextView tvProfitAmount;

    @Bind(R.id.tv_boss_total_purchase)
    TextView tvTotalPurchase;
    @Bind(R.id.tv_boss_advance_purchase)
    TextView tvAdvancePurchase;
    @Bind(R.id.tv_boss_station_purchase)
    TextView tvStationPurchase;
    @Bind(R.id.tv_boss_station_card_purchase)
    TextView tvCardPurchase;

    @Bind(R.id.tv_boss_total_amount)
    TextView tvTotalAmount;
    @Bind(R.id.tv_boss_advance_amount)
    TextView tvAdvanceAmount;
    @Bind(R.id.tv_boss_station_amount)
    TextView tvStationAmount;
    @Bind(R.id.tv_boss_station_card_amount)
    TextView tvCardAmount;
    @Bind(R.id.srl_boss)
    SwipeRefreshLayout refresh;
    @Bind(R.id.rv_boss)
    RecyclerView rv;
    @Bind(R.id.rv_boss_header)
    RecyclerViewHeader header;
    private int page = 1;
    private BossAdapter adapter;
    private TimePickerView timeView;
    private Dialog exitDialog;
    private String selectTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss);
        ButterKnife.bind(this);
        presenter = new BossPresenter(this);
        initView();
    }
    private void initView() {
        adapter = new BossAdapter(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        header.attachTo(rv);
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new OnRvScrollListener() {
            @Override
            public void toBottom() {
                super.toBottom();
                page++;
                presenter.getMsg(selectTime, page);
            }
        });
        presenter.initSalendar();
        timeView = new TimePickerView(this,TimePickerView.Type.YEAR_MONTH_DAY);
        timeView.setCancelable(true);
        timeView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String d = format.format(date);
                setSalendar(d);
                page = 1;
                presenter.getMsg(d, page);
            }
        });
        timeView.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                ivSalendar.setImageResource(R.drawable.ic_boss_calendarclose);
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                presenter.getMsg(selectTime, page);
            }
        });
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_boss_exit:
                exitDialog = MyDialog.getAlertDialog(this, "确定退出?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
                break;
            case R.id.ll_boss_calendar:
                timeView.show();
                ivSalendar.setImageResource(R.drawable.ic_boss_calendarup);
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }
    private void logout() {
        PrefUtil.putBoolean(CommonCode.SP_IS_LOGIN_BOSS, false);
        exitDialog.dismiss();
        Intent intent = new Intent(this,LoginControllerActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void showToast(String toast) {
        refresh.setRefreshing(false);
        ToastUtil.show(toast);
    }
    @Override
    public void setSalendar(String salendar) {
        tvCalendar.setText(salendar);
        selectTime = salendar;
    }
    @Override
    public void getProfitSuccess(String profit) {
        refresh.setRefreshing(false);
        tvProfitAmount.setText(profit);
    }
    @Override
    public void getTodayMsgAmountSuccess(BossTodayAmount amount) {
        refresh.setRefreshing(false);
        tvTotalAmount.setText(amount.total_amount+"元");
        tvAdvanceAmount.setText("现金:"+amount.advance_amount+"元");
        tvStationAmount.setText("平台:" + amount.station_amount + "元");
        tvCardAmount.setText("油卡:"+amount.card_amount+"元");
    }
    @Override
    public void getTodayMsgPurchaseSuccess(BossTodayPurchase purchase) {
        refresh.setRefreshing(false);
        tvTotalPurchase.setText(purchase.total_purchase + "L");
        tvAdvancePurchase.setText("现金:"+purchase.advance_purchase+"L");
        tvStationPurchase.setText("平台:"+purchase.station_purchase+"L");
        tvCardPurchase.setText("油卡:"+purchase.card_purchase+"L");
    }
    @Override
    public void getMsgEmployee(List<BossMsgEmployeeDetail> list) {
        if (page == 1){
            adapter.setData(list, selectTime);
        } else {
            adapter.addData(list, selectTime);
        }
        adapter.notifyDataSetChanged();
        refresh.setRefreshing(false);
    }
}