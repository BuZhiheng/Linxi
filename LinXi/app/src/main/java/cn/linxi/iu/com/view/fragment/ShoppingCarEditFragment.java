package cn.linxi.iu.com.view.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.x;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.Shared;
import cn.linxi.iu.com.model.ShoppingCar;
import cn.linxi.iu.com.presenter.ShoppingCarEditPresenter;
import cn.linxi.iu.com.presenter.ShoppingCarPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IShoppingCarEditPresenter;
import cn.linxi.iu.com.util.BitmapUtil;
import cn.linxi.iu.com.util.ShareManager;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IShoppingCarEditView;
import cn.linxi.iu.com.view.widget.SharePopupWindow;

/**
 * Created by buzhiheng on 2017/3/16.
 */
public class ShoppingCarEditFragment extends Fragment implements IShoppingCarEditView, View.OnClickListener {
    private IShoppingCarEditPresenter presenter;
    private View view;
    @Bind(R.id.ll_shoppingcar_content)
    LinearLayout layout;
    @Bind(R.id.tv_shoppingcar_delete)
    TextView tvDelete;
    @Bind(R.id.tv_shoppingcar_share)
    TextView tvShare;
    private SharePopupWindow popWin;
    private ShareManager manager;
    private Shared share;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view != null){
            return view;
        }
        view = inflater.inflate(R.layout.fragment_shopping_car_edit,container,false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }
    private void initView() {
        presenter = new ShoppingCarEditPresenter(this);
        tvDelete.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        popWin = new SharePopupWindow((AppCompatActivity) getActivity(),this);
        manager = ShareManager.getInstance((AppCompatActivity) getActivity());
    }
    @Override
    public void onResume() {
        super.onResume();
        layout.removeAllViews();
        presenter.getShoppingCar();
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void addItem(final ShoppingCar car) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_shopping_car_item_edit,null);
        TextView tvName = (TextView) view.findViewById(R.id.tv_shoppingcar_item_type);
        TextView tvOrderType = (TextView) view.findViewById(R.id.tv_shoppingcar_item_ordertype);
        final EditText et = (EditText) view.findViewById(R.id.et_shoppingcar_item_cout);
        final ImageView ivSelect = (ImageView) view.findViewById(R.id.iv_shoppingcar_item_select);
        ImageView ivSub = (ImageView) view.findViewById(R.id.iv_shoppingcar_item_sub);
        ImageView ivAdd = (ImageView) view.findViewById(R.id.iv_shoppingcar_item_add);
        ImageView ivPic = (ImageView) view.findViewById(R.id.iv_shoppingcar_item_goods);
        tvName.setText(car.title);
        et.setText(car.num + "");
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText(presenter.addGoodsCout(et, car));
            }
        });
        ivSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText(presenter.subGoodsCout(et, car));
            }
        });
        ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSelectClick(new ShoppingCarEditPresenter.OnSelectClick() {
                    @Override
                    public void setSelect() {
                        ivSelect.setImageResource(R.drawable.ic_station_checked);
                    }
                    @Override
                    public void setUnSelect() {
                        ivSelect.setImageResource(R.drawable.ic_station_check);
                    }
                }, ivSelect, car);
            }
        });
        if (car.orderType == 1){
            tvOrderType.setVisibility(View.VISIBLE);
            tvOrderType.setText("油品汇总");
        } else if (car.orderType == 2){
            tvOrderType.setVisibility(View.VISIBLE);
            tvOrderType.setText("非油品汇总");
        }
        x.image().bind(ivPic,car.pic, BitmapUtil.getOptionCommon());
        layout.addView(view);
    }
    @Override
    public void removeView() {
        layout.removeAllViews();
    }
    public void updateShopping(ShoppingCarEditPresenter.OnUpdateListner listner){
        presenter.updateShoppingCar(listner);
    }
    public void setShare(Shared share) {
        this.share = share;
        popWin.showAtLocation(getActivity().findViewById(R.id.ll_shoppingcar_edit), Gravity.BOTTOM, 0, 0);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_shoppingcar_delete:
                presenter.deleteShoppingCar();
                break;
            case R.id.tv_shoppingcar_share:
                presenter.setShare();
                break;
            case R.id.ll_popwinshare_qq:
                manager.shareQQ(share);
                popWin.dismiss();
                break;
            case R.id.ll_popwinshare_qzone:
                share.setWxType(manager.SHARE_TYPE_SQUARE);
                manager.shareQQ(share);
                popWin.dismiss();
                break;
            case R.id.ll_popwinshare_wx:
                share.setWxType(manager.SHARE_TYPE_CHAT);
                manager.shareWx(share);
                popWin.dismiss();
                break;
            case R.id.ll_popwinshare_wxsquare:
                share.setWxType(manager.SHARE_TYPE_SQUARE);
                manager.shareWx(share);
                popWin.dismiss();
                break;
            case R.id.tv_popwinshare_cancel:
                popWin.dismiss();
                break;
        }
    }
}