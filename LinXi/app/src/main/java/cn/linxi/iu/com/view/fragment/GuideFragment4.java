package cn.linxi.iu.com.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.view.activity.LoginControllerActivity;

/**
 * Created by buzhiheng on 2016/10/10.
 *
 */
public class GuideFragment4 extends Fragment implements View.OnClickListener {
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null){
            return view;
        }
        view = inflater.inflate(R.layout.fragment_guide4,container,false);
        initView();
        return view;
    }
    private void initView() {
        view.findViewById(R.id.btn_guide_start).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_guide_start:
                PrefUtil.putBoolean(CommonCode.SP_IS_STARTED,true);
                Intent intent = new Intent(getActivity(),LoginControllerActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }
}