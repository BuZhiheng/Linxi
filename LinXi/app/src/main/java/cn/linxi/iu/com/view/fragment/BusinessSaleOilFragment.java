package cn.linxi.iu.com.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
/**
 * Created by buzhiheng on 2017/4/25.
 */
public class BusinessSaleOilFragment extends Fragment {
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_business_sale_oil,container,false);
        }
        ButterKnife.bind(this,view);
        return view;
    }
}