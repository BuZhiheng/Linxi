package cn.linxi.iu.com.adapter;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;
import cn.linxi.iu.com.R;
/**
 * Created by buzhiheng on 2016/8/9.
 */
public class FragmentPagerChangeListener implements ViewPager.OnPageChangeListener {
    private Context context;
    private TextView btnUnFinish;
    private TextView btnHistory;
    private ImageView ivUnFinish;
    private ImageView ivHistory;
    public FragmentPagerChangeListener (Context context,TextView btnUnFinish,TextView btnHistory,ImageView ivUnFinish,ImageView ivHistory){
        this.context = context;
        this.btnUnFinish = btnUnFinish;
        this.btnHistory = btnHistory;
        this.ivUnFinish = ivUnFinish;
        this.ivHistory = ivHistory;
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                btnUnFinish.setTextColor(ContextCompat.getColor(context, R.color.color_main));
                ivUnFinish.setImageResource(R.color.color_main);
                btnHistory.setTextColor(ContextCompat.getColor(context, R.color.color_black_text));
                ivHistory.setImageResource(R.color.color_white);
                break;
            case 1:
                btnUnFinish.setTextColor(ContextCompat.getColor(context, R.color.color_black_text));
                ivUnFinish.setImageResource(R.color.color_white);
                btnHistory.setTextColor(ContextCompat.getColor(context, R.color.color_main));
                ivHistory.setImageResource(R.color.color_main);
                break;
        }
    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
