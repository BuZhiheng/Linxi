package cn.linxi.iu.com.view.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.linxi.iu.com.R;
/**
 * Created by buzhiheng on 2016/10/10.
 *
 */
public class GuideFragment3 extends Fragment{
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null){
            return view;
        }
        view = inflater.inflate(R.layout.fragment_guide3,container,false);
        return view;
    }
}