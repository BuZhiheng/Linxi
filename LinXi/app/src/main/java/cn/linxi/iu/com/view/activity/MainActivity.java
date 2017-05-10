package cn.linxi.iu.com.view.activity;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.EventLogout;
import cn.linxi.iu.com.model.SelectCity;
import cn.linxi.iu.com.model.TIModelCustomerOrderSure;
import cn.linxi.iu.com.model.UpdateMsg;
import cn.linxi.iu.com.presenter.MainPresenter;
import cn.linxi.iu.com.presenter.TIMPresenter;
import cn.linxi.iu.com.presenter.UpdatePresenter;
import cn.linxi.iu.com.presenter.ipresenter.IMainPresenter;
import cn.linxi.iu.com.presenter.ipresenter.ITIMPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IUpdatePresenter;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.fragment.BuyFragment;
import cn.linxi.iu.com.view.fragment.MineFragment;
import cn.linxi.iu.com.view.fragment.ShowCodeFragment;
import cn.linxi.iu.com.view.fragment.TransferMarketFragment;
import cn.linxi.iu.com.view.iview.IMainView;
import cn.linxi.iu.com.view.iview.ITIMView;
import cn.linxi.iu.com.view.iview.IUpdateView;
import cn.linxi.iu.com.view.widget.UpdateDialog;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
public class MainActivity extends AppCompatActivity implements IMainView,ITIMView,IUpdateView{
    private ITIMPresenter presenter;
    private IUpdatePresenter updatePresenter;
    private IMainPresenter mainPresenter;
    @Bind(R.id.iv_main_bottom_buy)
    ImageView ivBuy;
    @Bind(R.id.iv_main_bottom_code)
    ImageView ivCode;
    @Bind(R.id.iv_main_bottom_sale)
    ImageView ivSale;
    @Bind(R.id.iv_main_bottom_mine)
    ImageView ivMine;
    @Bind(R.id.tv_main_bottom_buy)
    TextView tvBuy;
    @Bind(R.id.tv_main_bottom_code)
    TextView tvCode;
    @Bind(R.id.tv_main_bottom_sale)
    TextView tvSale;
    @Bind(R.id.tv_main_bottom_mine)
    TextView tvMine;
    @Bind(R.id.fl_first_togift_guide)
    FrameLayout flGuideGift;
    private boolean isLookGift = false;
    private FragmentManager fragmentManager;
    private List<Fragment> fragments;
    private BuyFragment buyFragment = new BuyFragment();
    private ShowCodeFragment codeFragment = new ShowCodeFragment();
    private TransferMarketFragment transferFragment = new TransferMarketFragment();
    private MineFragment mineFragment = new MineFragment();
    private boolean canExit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }
    private void initView() {
        isLookGift = PrefUtil.getBoolean(CommonCode.SP_IS_LOOKED_GIFT,false);
        presenter = new TIMPresenter(this);
        updatePresenter = new UpdatePresenter(this);
        mainPresenter = new MainPresenter(this);
        presenter.timInit(this, CommonCode.TIM_INIT_TYPE_CLIENT);
        updatePresenter.getUpdateCommon();
        fragments = new ArrayList<>();
        fragments.add(buyFragment);
        fragments.add(codeFragment);
        fragments.add(transferFragment);
        fragments.add(mineFragment);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.id_main_content, buyFragment).commit();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (canExit) {
                finish();
                return true;
            }
            canExit = true;
            ToastUtil.show("再次点击退出");
            OkHttpUtil.executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        canExit = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventLogout logout){//登出
        //EventBus事件方法, 个人资料页面点击退出登录
        Intent intent = new Intent(this,LoginControllerActivity.class);
        startActivity(intent);
        finish();
    }
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(EventRelogin login){
//        //EventBus事件方法, 单点登录
//        Intent intent = new Intent(this,LoginActivity.class);
//        startActivity(intent);
//    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_main_bottom_buy:
                FragmentTransaction transaction1 = fragmentManager.beginTransaction();
                transaction1.replace(R.id.id_main_content,fragments.get(0)).commit();
                initBottom();
                ivBuy.setImageResource(R.drawable.ic_mainact_buyc);
                tvBuy.setTextColor(ContextCompat.getColor(this, R.color.color_main));
                break;
            case R.id.ll_main_bottom_code:
                FragmentTransaction transaction2 = fragmentManager.beginTransaction();
                transaction2.replace(R.id.id_main_content,fragments.get(1)).commit();
                initBottom();
                ivCode.setImageResource(R.drawable.ic_mainact_codec);
                tvCode.setTextColor(ContextCompat.getColor(this, R.color.color_main));
                break;
            case R.id.ll_main_bottom_sale:
                FragmentTransaction transaction3 = fragmentManager.beginTransaction();
                transaction3.replace(R.id.id_main_content,fragments.get(2)).commit();
                initBottom();
                ivSale.setImageResource(R.drawable.ic_mainact_salec);
                tvSale.setTextColor(ContextCompat.getColor(this, R.color.color_main));
                break;
            case R.id.ll_main_bottom_mine:
                if (!isLookGift){
                    flGuideGift.setVisibility(View.VISIBLE);
                }
                FragmentTransaction transaction4 = fragmentManager.beginTransaction();
                transaction4.replace(R.id.id_main_content,fragments.get(3)).commit();
                initBottom();
                ivMine.setImageResource(R.drawable.ic_mainact_minec);
                tvMine.setTextColor(ContextCompat.getColor(this, R.color.color_main));
                break;
            case R.id.iv_first_togift_iknow:
                flGuideGift.setVisibility(View.GONE);
                isLookGift = true;
                PrefUtil.putBoolean(CommonCode.SP_IS_LOOKED_GIFT,true);
                break;
            default:break;
        }
    }
    private void initBottom(){
        ivBuy.setImageResource(R.drawable.ic_mainact_buy);
        ivCode.setImageResource(R.drawable.ic_mainact_code);
        ivSale.setImageResource(R.drawable.ic_mainact_sale);
        ivMine.setImageResource(R.drawable.ic_mainact_mine);
        tvBuy.setTextColor(ContextCompat.getColor(this, R.color.color_black_text));
        tvCode.setTextColor(ContextCompat.getColor(this, R.color.color_black_text));
        tvSale.setTextColor(ContextCompat.getColor(this, R.color.color_black_text));
        tvMine.setTextColor(ContextCompat.getColor(this, R.color.color_black_text));
    }
    @Override
    public void timLoginSuccess() {
        mainPresenter.getUnfinishOrder();
    }
    @Override
    public void timLoginError() {
    }
    @Override
    public void timOnForceOffline() {
        ToastUtil.show("您的账号在其他设备登录");
        Intent intent = new Intent(this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        PrefUtil.putBoolean(CommonCode.SP_IS_LOGIN, false);
    }
    @Override
    public void timOnUserSigExpired() {
        presenter.timInit(this, CommonCode.TIM_INIT_TYPE_CLIENT);
    }
    @Override
    public void timOrderSure(TIModelCustomerOrderSure order) {
        Intent intent = new Intent(this,CustomerOrderSureActivity.class);
        intent.putExtra(CommonCode.INTENT_REGISTER_USER, order);
        startActivity(intent);
    }
    @Override
    public void showVoice() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
    }
    @Override
    public void showVib() {
        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        long [] pattern = {100,400,100,400};   // 停止 开启 停止 开启
        vibrator.vibrate(pattern, -1);
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
//    @Override
//    public void refreshBuy(SelectCity city) {
//        buyFragment.onRefresh(city);
//    }
    @Override
    public void showUpdate(UpdateMsg msg) {
        UpdateDialog dialog = new UpdateDialog(this,msg);
        dialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case CommonCode.ACTIVITY_RESULT_CODE_BUY:
                if (data != null && data.getSerializableExtra(CommonCode.INTENT_REGISTER_USER) != null){
                    SelectCity city = (SelectCity) data.getSerializableExtra(CommonCode.INTENT_REGISTER_USER);
                    buyFragment.onRefresh(city);
                }
                break;
            case CommonCode.ACTIVITY_RESULT_CODE_MINE:
                FragmentTransaction transaction1 = fragmentManager.beginTransaction();
                transaction1.replace(R.id.id_main_content,fragments.get(0)).commit();
                initBottom();
                ivBuy.setImageResource(R.drawable.ic_mainact_buyc);
                tvBuy.setTextColor(ContextCompat.getColor(this, R.color.color_main));
                break;
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
    @PermissionSuccess(requestCode = 100)
    public void doSomething(){
//        mainPresenter.location();
        buyFragment.initBuy();
        ToastUtil.show("获得权限");
    }
    @PermissionFail(requestCode = 100)
    public void doFailSomething(){
        SelectCity city = new SelectCity();
        city.city_code = 0;
        city.city_name = "位置";
        buyFragment.onRefresh(city);
//        ToastUtil.show("拒绝");
    }
}