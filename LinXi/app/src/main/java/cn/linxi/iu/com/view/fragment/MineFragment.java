package cn.linxi.iu.com.view.fragment;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.xutils.x;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.Shared;
import cn.linxi.iu.com.model.UserCenterInfo;
import cn.linxi.iu.com.presenter.MineFrmPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IMineFrmPresenter;
import cn.linxi.iu.com.util.BitmapUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.ShareManager;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.activity.BalanceActivity;
import cn.linxi.iu.com.view.activity.BindPhoneActivity;
import cn.linxi.iu.com.view.activity.CouponActivity;
import cn.linxi.iu.com.view.activity.FeedBackActivity;
import cn.linxi.iu.com.view.activity.GiftActivity;
import cn.linxi.iu.com.view.activity.MyOilCardActivity;
import cn.linxi.iu.com.view.activity.MyOrderActivity;
import cn.linxi.iu.com.view.activity.PersonalMsgActivity;
import cn.linxi.iu.com.view.activity.QRCodeShowActivity;
import cn.linxi.iu.com.view.activity.SafeCenterActivity;
import cn.linxi.iu.com.view.activity.SetupActivity;
import cn.linxi.iu.com.view.activity.SignActivity;
import cn.linxi.iu.com.view.activity.VipCenterActivity;
import cn.linxi.iu.com.view.activity.WaitForOpenActivity;
import cn.linxi.iu.com.view.iview.IMineFrmView;
import cn.linxi.iu.com.view.widget.SharePopupWindow;
/**
 * Created by buzhiheng on 2016/7/15.
 */
public class MineFragment extends Fragment implements View.OnClickListener,IMineFrmView {
    private IMineFrmPresenter presenter;
    private final int TIME_OUT = 0X001;
    @Bind(R.id.ll_minefrm_cash)
    LinearLayout llCash;
    @Bind(R.id.ll_minefrm_order)
    LinearLayout llOrder;
    @Bind(R.id.ll_minefrm_sign)
    LinearLayout llSign;
    @Bind(R.id.ll_minefrm_myoilcard)
    LinearLayout llMycard;
    @Bind(R.id.ll_minefrm_coupon)
    LinearLayout llCoupon;
    @Bind(R.id.fl_minefrm_gift)
    FrameLayout flGift;
    @Bind(R.id.ll_minefrm_setup)
    LinearLayout llSetup;
    @Bind(R.id.fl_minefrm_user)
    FrameLayout flUser;
    @Bind(R.id.fl_minefrm_safe)
    FrameLayout flSafe;
    @Bind(R.id.tv_minefrm_vipdesc)
    TextView tvVipdesc;
    @Bind(R.id.tv_minefrm_phone)
    TextView tvPhone;
    @Bind(R.id.tv_minefrm_balance)
    TextView tvBalance;
    @Bind(R.id.iv_minefrm_photo)
    ImageView ivPhoto;
    @Bind(R.id.iv_minefrm_vip)
    ImageView ivVip;
    @Bind(R.id.srl_minefrm)
    SwipeRefreshLayout refresh;
    private SharePopupWindow popWin;
    private ShareManager manager;
    private Shared share;
    private UserCenterInfo info;
    private View view;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TIME_OUT:
                    refresh.setRefreshing(false);
                    showToast("连接超时");
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null){
            return view;
        }
        view = inflater.inflate(R.layout.fragment_mine,container,false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }
    private void initView() {
        refresh.setProgressViewOffset(false, CommonCode.OFFSET_START, CommonCode.OFFSET_END);
        presenter = new MineFrmPresenter(this);
        popWin = new SharePopupWindow((AppCompatActivity) getActivity(),this);
        manager = ShareManager.getInstance((AppCompatActivity) getActivity());
        llCash.setOnClickListener(this);
        flUser.setOnClickListener(this);
        llOrder.setOnClickListener(this);
        llSign.setOnClickListener(this);
        llMycard.setOnClickListener(this);
        flGift.setOnClickListener(this);
        llCoupon.setOnClickListener(this);
        llSetup.setOnClickListener(this);
        flSafe.setOnClickListener(this);
        view.findViewById(R.id.ll_minefrm_share).setOnClickListener(this);
        view.findViewById(R.id.fl_minefrm_feedback).setOnClickListener(this);
        view.findViewById(R.id.fl_minefrm_vip).setOnClickListener(this);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getUserInfo(refresh);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        refresh.setRefreshing(true);
        presenter.getUserInfo(refresh);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fl_minefrm_user:
                Intent intentMsg = new Intent(getActivity(), PersonalMsgActivity.class);
                startActivity(intentMsg);
                break;
            case R.id.ll_minefrm_cash:
                Intent intent = new Intent(getActivity(), BalanceActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_minefrm_myoilcard:
                Intent intentDetail = new Intent(getActivity(), MyOilCardActivity.class);
                startActivity(intentDetail);
                break;
            case R.id.fl_minefrm_gift:
                Intent intentGift = new Intent(getActivity(), GiftActivity.class);
                startActivity(intentGift);
                break;
            case R.id.ll_minefrm_coupon:
                if (PrefUtil.getBoolean(CommonCode.SP_WAIT_IS_OPEN_ENVELOP,false)){
                    Intent intentCoupon = new Intent(getActivity(), CouponActivity.class);
                    startActivity(intentCoupon);
                    return;
                }
                Intent intentOpen = new Intent(getActivity(), WaitForOpenActivity.class);
                startActivity(intentOpen);
                break;
            case R.id.ll_minefrm_sign:
                if (PrefUtil.getBoolean(CommonCode.SP_WAIT_IS_OPEN_PAST,false)){
                    Intent intentSign = new Intent(getActivity(), SignActivity.class);
                    startActivity(intentSign);
                    return;
                }
                Intent intentOpenSign = new Intent(getActivity(), WaitForOpenActivity.class);
                startActivity(intentOpenSign);
                break;
            case R.id.ll_minefrm_order:
                Intent intentOrder = new Intent(getActivity(), MyOrderActivity.class);
                startActivity(intentOrder);
                break;
            case R.id.ll_minefrm_setup:
                Intent intentSetup = new Intent(getActivity(), SetupActivity.class);
                startActivity(intentSetup);
                break;
            case R.id.fl_minefrm_feedback:
                Intent intentFeedback = new Intent(getActivity(), FeedBackActivity.class);
                startActivity(intentFeedback);
                break;
            case R.id.ll_minefrm_share:
                if (info == null){
                    ToastUtil.show("用户信息获取失败");
                    return;
                }
                share = new Shared();
                share.setTitle(info.share_title);
                share.setDesc(info.share_describe);
                share.setUrl(info.share_url);
                share.setImgUrl(CommonCode.APP_ICON);
                popWin.showAtLocation(getActivity().findViewById(R.id.fl_main), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.fl_minefrm_safe:
                if (StringUtil.isNull(PrefUtil.getString(CommonCode.SP_USER_PHONE, ""))){
                    Intent intentBind = new Intent(getActivity(), BindPhoneActivity.class);
                    startActivity(intentBind);
                    return;
                }
                Intent intentSafe = new Intent(getActivity(), SafeCenterActivity.class);
                startActivity(intentSafe);
                break;
            case R.id.fl_minefrm_vip:
                Intent intentVip = new Intent(getActivity(), VipCenterActivity.class);
                startActivityForResult(intentVip, CommonCode.ACTIVITY_RESULT_CODE_MINE);
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
            default:
                break;
        }
    }
    @Override
    public void showToast(String toast) {
        refresh.setRefreshing(false);
        ToastUtil.show(toast);
    }
    @Override
    public void getInfoSuccess(UserCenterInfo info) {
        this.info = info;
        refresh.setRefreshing(false);
        tvVipdesc.setText(info.vip_desc);
        tvPhone.setText(info.mobile);
        tvBalance.setText(info.balance+"");
        x.image().bind(ivPhoto, info.avatar, BitmapUtil.getOptionRadius(30));
    }
    @Override
    public void timeOut() {
        handler.sendEmptyMessage(TIME_OUT);
    }
    @Override
    public void isVip(int vipIc,String vipDsc) {
        ivVip.setImageResource(vipIc);
    }
}