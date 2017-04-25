package cn.linxi.iu.com.view.fragment;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.OperatUser;
import cn.linxi.iu.com.model.TIModelCustomerOrderSure;
import cn.linxi.iu.com.presenter.BusinessMainPresenter;
import cn.linxi.iu.com.presenter.TIMPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessMainPresenter;
import cn.linxi.iu.com.presenter.ipresenter.ITIMPresenter;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.activity.BusinessLoginActivity;
import cn.linxi.iu.com.view.activity.BusinessOrdersListActivity;
import cn.linxi.iu.com.view.activity.BusinessPreSaleActivity;
import cn.linxi.iu.com.view.activity.BusinessShareQrcodeActivity;
import cn.linxi.iu.com.view.activity.LoginControllerActivity;
import cn.linxi.iu.com.view.iview.IBusinessMainView;
import cn.linxi.iu.com.view.iview.ITIMView;
import cn.linxi.iu.com.view.widget.BusinessWorkOutDialog;
import cn.linxi.iu.com.view.widget.MyDialog;
/**
 * Created by buzhiheng on 2016/12/3.
 */
public class BusinessMainFragment extends Fragment implements IBusinessMainView,ITIMView,View.OnClickListener {
    private View view;
    private IBusinessMainPresenter presenter;
    private ITIMPresenter presenterTIM;
    @Bind(R.id.srl_business_mainfrm)
    SwipeRefreshLayout refresh;
    @Bind(R.id.sv_business_mainfrm)
    ScrollView scrollView;
    @Bind(R.id.tv_businessmain_name)
    TextView tvName;
    @Bind(R.id.tv_businessmain_amount)
    TextView tvAmount;//收款总额
    @Bind(R.id.tv_businessmain_oil)
    TextView tvOil;//加油总量
    @Bind(R.id.tv_businessmain_gas)
    TextView tvGas;//加气总量
    @Bind(R.id.tv_businessmain_command_code)
    TextView tvCommandCode;
    private OperatUser pur;
    private Dialog dialog;
    private Dialog dialogNotice;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null){
            return view;
        }
        view = inflater.inflate(R.layout.fragment_business_main,container,false);
        ButterKnife.bind(this, view);
        presenter = new BusinessMainPresenter(this);
        presenterTIM = new TIMPresenter(this);
        initView();
        return view;
    }
    private void initView() {
        presenter.getOperateUser();
        presenterTIM.timInit(getContext(), CommonCode.TIM_INIT_TYPE_STATION);
        view.findViewById(R.id.fl_business_presale).setOnClickListener(this);
        view.findViewById(R.id.fl_business_toorders).setOnClickListener(this);
        view.findViewById(R.id.btn_business_workoff).setOnClickListener(this);
        view.findViewById(R.id.iv_business_share).setOnClickListener(this);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getOperateUser();
            }
        });
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (refresh != null) {
                    refresh.setEnabled(scrollView.getScrollY() == 0);
                }
            }
        });
    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_business_share:
                Intent intentShare = new Intent(getContext(),BusinessShareQrcodeActivity.class);
                startActivity(intentShare);
                break;
            case R.id.fl_business_presale:
                Intent intentPreSale = new Intent(getContext(),BusinessPreSaleActivity.class);
                startActivity(intentPreSale);
                break;
            case R.id.fl_business_toorders:
                Intent intentOrders = new Intent(getContext(),BusinessOrdersListActivity.class);
                startActivity(intentOrders);
                break;
            case R.id.btn_business_workoff:
                dialog = new BusinessWorkOutDialog(getContext(), pur, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.checkWorkOut();
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
        }
    }
    @Override
    public void workOut(){
        PrefUtil.putBoolean(CommonCode.SP_IS_LOGIN_BUSINESS, false);
        Intent intent = new Intent(getActivity(),LoginControllerActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
    @Override
    public void cantWorkOut() {
        dialogNotice = MyDialog.getAlertDialog(getContext(), "请先去后台结算", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogNotice.dismiss();
            }
        });
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
        refresh.setRefreshing(false);
    }
    @Override
    public void getUserSuccess(OperatUser user) {
        tvName.setText(user.username);
        tvAmount.setText(user.advance_amount);
        tvOil.setText(user.station_purchase);
        tvGas.setText(user.station_gas);
        tvCommandCode.setText("推广编号:"+user.extension_number);
        pur = user;
        refresh.setRefreshing(false);
    }
    @Override
    public void timLoginSuccess() {
    }
    @Override
    public void timLoginError() {
    }
    @Override
    public void timOnForceOffline() {//被踢下线
        ToastUtil.show("您的账号在其他设备登录");
        Intent intent = new Intent(getActivity(),BusinessLoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        PrefUtil.putBoolean(CommonCode.SP_IS_LOGIN_BUSINESS, false);
    }
    @Override
    public void timOnUserSigExpired() {
        presenterTIM.timInit(getContext(), CommonCode.TIM_INIT_TYPE_STATION);
    }
    @Override
    public void timOrderSure(TIModelCustomerOrderSure order) {
    }
    @Override
    public void showVoice() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getContext(), notification);
        r.play();
    }
    @Override
    public void showVib() {
        Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        long [] pattern = {100,400,100,400};   // 停止 开启 停止 开启
        vibrator.vibrate(pattern, -1);
    }
}