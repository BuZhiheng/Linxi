package cn.linxi.iu.com.view.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.presenter.BusinessPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.fragment.BusinessIndexFragment;
import cn.linxi.iu.com.view.fragment.BusinessMineFragment;
import cn.linxi.iu.com.view.iview.IBusinessView;
/**
 * Created by buzhiheng on 2016/12/3.
 */
public class BusinessActivity extends AppCompatActivity implements IBusinessView {
    private IBusinessPresenter presenter;
    private FragmentManager fragmentManager;
    private List<Fragment> fragments;
    private Fragment fMain = new BusinessIndexFragment();
    private Fragment fMine = new BusinessMineFragment();
    @Bind(R.id.iv_business_main_index)
    ImageView ivMain;
    @Bind(R.id.iv_business_main_mine)
    ImageView ivMine;
    @Bind(R.id.tv_business_main_index)
    TextView tvMain;
    @Bind(R.id.tv_business_main_mine)
    TextView tvMine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_main);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        presenter = new BusinessPresenter(this);
        fragments = new ArrayList<>();
        fragments.add(fMain);
        fragments.add(fMine);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.id_business_main_content, fMain).commit();
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_business_main_scan:
                presenter.checkPermissionCamera();
                break;
            case R.id.ll_business_main:
                ivMain.setImageResource(R.drawable.ic_business_mainc);
                ivMine.setImageResource(R.drawable.ic_mainact_mine);
                tvMain.setTextColor(ContextCompat.getColor(this, R.color.color_main));
                tvMine.setTextColor(ContextCompat.getColor(this,R.color.color_black_text));
                FragmentTransaction transaction1 = fragmentManager.beginTransaction();
                transaction1.replace(R.id.id_business_main_content,fragments.get(0)).commit();
                break;
            case R.id.ll_business_mine:
                ivMain.setImageResource(R.drawable.ic_business_main);
                ivMine.setImageResource(R.drawable.ic_mainact_minec);
                tvMain.setTextColor(ContextCompat.getColor(this, R.color.color_black_text));
                tvMine.setTextColor(ContextCompat.getColor(this, R.color.color_main));
                FragmentTransaction transaction2 = fragmentManager.beginTransaction();
                transaction2.replace(R.id.id_business_main_content,fragments.get(1)).commit();
                break;
        }
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void toQRScanView() {
        Intent intentScan = new Intent(this,MyCodeScanAvtivity.class);
        startActivityForResult(intentScan, 0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case CommonCode.ACTIVITY_RESULT_CODE_QRCODE:
                Intent intent = new Intent(this,BusinessAfterscanActivity.class);
                intent.putExtra(CommonCode.INTENT_QRCODE, data.getStringExtra(CommonCode.INTENT_QRCODE));
                startActivity(intent);
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }
}