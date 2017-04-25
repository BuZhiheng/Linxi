package cn.linxi.iu.com.view.activity;
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
import cn.linxi.iu.com.view.fragment.BusinessSaleGoodsFragment;
import cn.linxi.iu.com.view.fragment.BusinessSaleOilFragment;
import cn.linxi.iu.com.view.iview.IOilDetailTypeView;
public class BusinessSaleMoneyActivity extends AppCompatActivity implements IOilDetailTypeView {
    @Bind(R.id.vp_business_sale_money)
    ViewPager vp;
    @Bind(R.id.tv_business_sale_oil)
    TextView tvOil;
    @Bind(R.id.tv_business_sale_goods)
    TextView tvGoods;
    @Bind(R.id.iv_business_sale_oil)
    ImageView ivOil;
    @Bind(R.id.iv_business_sale_goods)
    ImageView ivGoods;
    @Bind(R.id.ll_business_sale_money_title)
    LinearLayout llTitle;
    private List<Fragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_sale_money);
        ButterKnife.bind(this);
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
            case R.id.ll_business_sale_oil:
                vp.setCurrentItem(0);
                break;
            case R.id.tv_business_sale_goods:
                vp.setCurrentItem(1);
                break;
        }
    }
    @Override
    public void showOilFrm() {
        llTitle.setVisibility(View.GONE);
        fragments = new ArrayList<>();
        fragments.add(new BusinessSaleOilFragment());
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), fragments));
        vp.setCurrentItem(0);
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("油品");
    }
    @Override
    public void showGoodsFrm() {
        llTitle.setVisibility(View.GONE);
        fragments = new ArrayList<>();
        fragments.add(new BusinessSaleGoodsFragment());
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), fragments));
        vp.setCurrentItem(0);
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("非油品");
    }
    @Override
    public void showBothFrm() {
        fragments = new ArrayList<>();
        fragments.add(new BusinessSaleOilFragment());
        fragments.add(new BusinessSaleGoodsFragment());
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), fragments));
        vp.setCurrentItem(0);
        vp.addOnPageChangeListener(new FragmentPagerChangeListener(this,tvOil,tvGoods,ivOil,ivGoods));
    }
}