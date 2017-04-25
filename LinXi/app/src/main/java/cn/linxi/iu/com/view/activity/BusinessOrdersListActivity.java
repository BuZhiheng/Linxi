package cn.linxi.iu.com.view.activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.adapter.FragmentPagerAdapter;
import cn.linxi.iu.com.adapter.FragmentPagerChangeListener;
import cn.linxi.iu.com.view.fragment.BusinessOrderHistoryFragment;
import cn.linxi.iu.com.view.fragment.BusinessOrderUnFinishFragment;
/**
 * Created by buzhiheng on 2016/8/11.
 */
public class BusinessOrdersListActivity extends AppCompatActivity {
    @Bind(R.id.vp_myorder)
    ViewPager vp;
    @Bind(R.id.btn_myorder_unfinish)
    TextView btnUnFinish;
    @Bind(R.id.btn_myorder_history)
    TextView btnHistory;
    @Bind(R.id.iv_myorder_unfinish)
    ImageView ivUnFinish;
    @Bind(R.id.iv_myorder_history)
    ImageView ivHistory;
    private List<Fragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("加油收款明细");
        fragments = new ArrayList<>();
        fragments.add(new BusinessOrderUnFinishFragment());
        fragments.add(new BusinessOrderHistoryFragment());
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), fragments));
        vp.setCurrentItem(0);
        vp.addOnPageChangeListener(new FragmentPagerChangeListener(this,btnUnFinish,btnHistory,ivUnFinish,ivHistory));
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
            case R.id.btn_myorder_unfinish:
                vp.setCurrentItem(0);
                break;
            case R.id.btn_myorder_history:
                vp.setCurrentItem(1);
                break;
        }
    }
}