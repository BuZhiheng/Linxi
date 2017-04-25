package cn.linxi.iu.com.view.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.adapter.FragmentPagerAdapter;
import cn.linxi.iu.com.adapter.FragmentPagerChangeListener;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.view.activity.BusinessSaleMoneyActivity;
import cn.linxi.iu.com.view.activity.LoginControllerActivity;

/**
 * Created by buzhiheng on 2017/4/13.
 */
public class BusinessIndexFragment extends Fragment implements View.OnClickListener {
    private View view;
    @Bind(R.id.vp_business_index)
    ViewPager vp;
    @Bind(R.id.tv_business_index_oil)
    TextView tvOil;
    @Bind(R.id.iv_business_index_oil)
    ImageView ivOil;
    @Bind(R.id.tv_business_index_goods)
    TextView tvGoods;
    @Bind(R.id.iv_business_index_goods)
    ImageView ivGoods;
    private List<Fragment> fragments = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null){
            return view;
        }
        view = inflater.inflate(R.layout.fragment_business_index,container,false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }
    private void initView() {
        fragments.add(new BusinessIndexOilFragment());
        fragments.add(new BusinessIndexGoodsFragment());
        vp.setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragments));
        vp.setCurrentItem(0);
        vp.addOnPageChangeListener(new FragmentPagerChangeListener(getContext(), tvOil, tvGoods, ivOil, ivGoods));
        tvOil.setOnClickListener(this);
        tvGoods.setOnClickListener(this);
        view.findViewById(R.id.tv_business_index_exit).setOnClickListener(this);
        view.findViewById(R.id.tv_business_index_sale).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_business_index_oil:
                vp.setCurrentItem(0);
                break;
            case R.id.tv_business_index_goods:
                vp.setCurrentItem(1);
                break;
            case R.id.tv_business_index_exit:
                PrefUtil.putBoolean(CommonCode.SP_IS_LOGIN_BUSINESS, false);
                Intent intent = new Intent(getActivity(),LoginControllerActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.tv_business_index_sale:
                Intent intentSale = new Intent(getContext(), BusinessSaleMoneyActivity.class);
                startActivity(intentSale);
                break;
        }
    }
}