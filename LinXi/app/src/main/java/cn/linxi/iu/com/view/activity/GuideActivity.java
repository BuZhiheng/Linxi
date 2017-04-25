package cn.linxi.iu.com.view.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.adapter.FragmentPagerAdapter;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.view.fragment.GuideFragment1;
import cn.linxi.iu.com.view.fragment.GuideFragment2;
import cn.linxi.iu.com.view.fragment.GuideFragment3;
import cn.linxi.iu.com.view.fragment.GuideFragment4;
/**
 * Created by buzhiheng on 2016/8/4.
 * Desc 我的订单页面
 */
public class GuideActivity extends AppCompatActivity {
    @Bind(R.id.vp_guide)
    ViewPager vp;
    @Bind(R.id.tv_guide_frm1)
    TextView tv1;
    @Bind(R.id.tv_guide_frm2)
    TextView tv2;
    @Bind(R.id.tv_guide_frm3)
    TextView tv3;
    @Bind(R.id.tv_guide_frm4)
    TextView tv4;
    @Bind(R.id.ll_guide_jump)
    LinearLayout llJump;
    private List<Fragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        fragments = new ArrayList<>();
        fragments.add(new GuideFragment1());
        fragments.add(new GuideFragment2());
        fragments.add(new GuideFragment3());
        fragments.add(new GuideFragment4());
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), fragments));
        vp.setCurrentItem(0);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        tv1.setBackgroundResource(R.drawable.bg_ll_guide_img);
                        tv2.setBackgroundResource(R.drawable.bg_ll_guide_img_gray);
                        tv3.setBackgroundResource(R.drawable.bg_ll_guide_img_gray);
                        tv4.setBackgroundResource(R.drawable.bg_ll_guide_img_gray);
                        break;
                    case 1:
                        tv1.setBackgroundResource(R.drawable.bg_ll_guide_img_gray);
                        tv2.setBackgroundResource(R.drawable.bg_ll_guide_img);
                        tv3.setBackgroundResource(R.drawable.bg_ll_guide_img_gray);
                        tv4.setBackgroundResource(R.drawable.bg_ll_guide_img_gray);
                        break;
                    case 2:
                        tv1.setBackgroundResource(R.drawable.bg_ll_guide_img_gray);
                        tv2.setBackgroundResource(R.drawable.bg_ll_guide_img_gray);
                        tv3.setBackgroundResource(R.drawable.bg_ll_guide_img);
                        tv4.setBackgroundResource(R.drawable.bg_ll_guide_img_gray);
                        llJump.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        tv1.setBackgroundResource(R.drawable.bg_ll_guide_img_gray);
                        tv2.setBackgroundResource(R.drawable.bg_ll_guide_img_gray);
                        tv3.setBackgroundResource(R.drawable.bg_ll_guide_img_gray);
                        tv4.setBackgroundResource(R.drawable.bg_ll_guide_img);
                        llJump.setVisibility(View.GONE);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
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
            case R.id.ll_guide_jump:
                PrefUtil.putBoolean(CommonCode.SP_IS_STARTED, true);
                Intent intent = new Intent(this,LoginControllerActivity.class);
                startActivity(intent);
                finish();
        }
    }
}