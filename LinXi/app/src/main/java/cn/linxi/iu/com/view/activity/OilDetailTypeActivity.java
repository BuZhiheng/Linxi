package cn.linxi.iu.com.view.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.adapter.FragmentPagerAdapter;
import cn.linxi.iu.com.adapter.FragmentPagerChangeListener;
import cn.linxi.iu.com.presenter.OilDetailTypePresenter;
import cn.linxi.iu.com.view.fragment.StationOilTypeAutomacFragment;
import cn.linxi.iu.com.view.fragment.StationOilTypeFragment;
import cn.linxi.iu.com.view.iview.IOilDetailTypeView;
/**
 * Created by buzhiheng on 2016/8/4.
 * Desc 中洽模式选择油品页面
 */
public class OilDetailTypeActivity extends AppCompatActivity implements IOilDetailTypeView {
    @Bind(R.id.vp_oildetail_type)
    ViewPager vp;
    @Bind(R.id.tv_oildetail_oil)
    TextView tvOil;
    @Bind(R.id.tv_oildetail_automac)
    TextView tvAutomac;
    @Bind(R.id.iv_oiltype_oil)
    ImageView ivOil;
    @Bind(R.id.iv_oiltype_automac)
    ImageView ivAutomac;
    @Bind(R.id.ll_oildetail_oil)
    LinearLayout llOil;
    @Bind(R.id.ll_oildetail_goods)
    LinearLayout llGoods;
    private List<Fragment> fragments;
    private StationOilTypeFragment fragmentOil = new StationOilTypeFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_detail_type);
        ButterKnife.bind(this);
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("商品列表");
        ((ImageView)findViewById(R.id.iv_titlebar_right)).setImageResource(R.drawable.ic_shopping_car);
        new OilDetailTypePresenter(this,getIntent());
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
            case R.id.ll_oildetail_oil:
                vp.setCurrentItem(0);
                fragmentOil.showSelect();
                break;
            case R.id.tv_oildetail_automac:
                vp.setCurrentItem(1);
                break;
            case R.id.iv_titlebar_right:
                Intent intent = new Intent(this,ShoppingCarFrmActivity.class);
                startActivity(intent);
        }
    }
    @Override
    public void showOilFrm() {
        llGoods.setVisibility(View.GONE);
        fragments = new ArrayList<>();
        fragments.add(fragmentOil);
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), fragments));
        vp.setCurrentItem(0);
    }
    @Override
    public void showGoodsFrm() {
        llOil.setVisibility(View.GONE);
        llGoods.setVisibility(View.GONE);
        fragments = new ArrayList<>();
        fragments.add(new StationOilTypeAutomacFragment());
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), fragments));
        vp.setCurrentItem(0);
    }
    @Override
    public void showBothFrm() {
        fragments = new ArrayList<>();
        fragments.add(fragmentOil);
        fragments.add(new StationOilTypeAutomacFragment());
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), fragments));
        vp.setCurrentItem(0);
        vp.addOnPageChangeListener(new FragmentPagerChangeListener(this,tvOil,tvAutomac,ivOil,ivAutomac));
    }
}