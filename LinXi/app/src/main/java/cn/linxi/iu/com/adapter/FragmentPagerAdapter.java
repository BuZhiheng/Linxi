package cn.linxi.iu.com.adapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import java.util.List;
/**
 * Created by buzhiheng on 2016/8/9.
 */
public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter{
    List<Fragment> list;
    public FragmentPagerAdapter(FragmentManager fm,List list) {
        super(fm);
        this.list = list;
    }
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }
    @Override
    public int getCount() {
        return list.size();
    }
}
