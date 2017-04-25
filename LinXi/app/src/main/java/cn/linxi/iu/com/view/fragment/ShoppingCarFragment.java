package cn.linxi.iu.com.view.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.xutils.x;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.OrderDetail;
import cn.linxi.iu.com.model.ShoppingCar;
import cn.linxi.iu.com.presenter.ShoppingCarPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IShoppingCarPresenter;
import cn.linxi.iu.com.util.BitmapUtil;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.activity.OrderActivity;
import cn.linxi.iu.com.view.iview.IShoppingCarView;
/**
 * Created by buzhiheng on 2017/3/16.
 */
public class ShoppingCarFragment extends Fragment implements IShoppingCarView ,View.OnClickListener{
    private IShoppingCarPresenter presenter;
    private View view;
    @Bind(R.id.ll_shoppingcar_content)
    LinearLayout layout;
    @Bind(R.id.tv_shoppingcar_money)
    TextView tvMoney;
    @Bind(R.id.tv_shoppingcar_topay)
    TextView tvToPay;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view != null){
            return view;
        }
        view = inflater.inflate(R.layout.fragment_shopping_car,container,false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        layout.removeAllViews();
        presenter.getShoppingCar();
    }
    private void initView() {
        presenter = new ShoppingCarPresenter(this);
        tvToPay.setOnClickListener(this);
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void addItem(final ShoppingCar car) {//
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_shopping_car_item,null);
        TextView tvOrderType = (TextView) view.findViewById(R.id.tv_shoppingcar_item_ordertype);
        TextView tvType = (TextView) view.findViewById(R.id.tv_shoppingcar_item_type);
        TextView tvPrice = (TextView) view.findViewById(R.id.tv_shoppingcar_item_price);
        TextView tvPast = (TextView) view.findViewById(R.id.tv_shoppingcar_item_pastprice);
        TextView tvDel = (TextView) view.findViewById(R.id.tv_shoppingcar_item_delete);
        ImageView ivPic = (ImageView) view.findViewById(R.id.iv_shoppingcar_item_goods);
        final TextView tvNum = (TextView) view.findViewById(R.id.tv_shoppingcar_item_gnum);
        final ImageView ivSelect = (ImageView) view.findViewById(R.id.iv_shoppingcar_item_select);
        ImageView ivAdd = (ImageView) view.findViewById(R.id.iv_automac_detail_add);
        ImageView ivSub = (ImageView) view.findViewById(R.id.iv_automac_detail_sub);
        final EditText editText = (EditText) view.findViewById(R.id.et_automac_detail_cout);
        final TextView tvOption = (TextView) view.findViewById(R.id.tv_shoppingcar_item_option);
        final TextView tvSure = (TextView) view.findViewById(R.id.tv_shoppingcar_item_sure);
        final FrameLayout flOption = (FrameLayout) view.findViewById(R.id.fl_shoppingcar_item_option);
        tvOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flOption.setVisibility(View.VISIBLE);
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flOption.setVisibility(View.GONE);
                //去更新购物车
                presenter.updateShoppingCar(new ShoppingCarPresenter.OnUpdateSuccess(){
                    @Override
                    public void success(ShoppingCar updateCar) {
                        car.num = updateCar.num;
                        tvNum.setText("x"+car.num);
                        editText.setText(car.num+"");
                    }
                },car,editText);
            }
        });
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(presenter.addGoodsCout(editText));
            }
        });
        ivSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(presenter.subGoodsCout(editText));
            }
        });
        tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.deleteShoppingCar(new ShoppingCarPresenter.OnDeleteSuccess() {
                    @Override
                    public void success() {
                        layout.removeAllViews();
                        presenter.getShoppingCar();
                    }
                }, car);
            }
        });
        ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSelectClick(new ShoppingCarPresenter.OnSelectClick() {
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
//        tvName.setText(car.title);
        if (car.orderType == 1){
            tvOrderType.setVisibility(View.VISIBLE);
            tvOrderType.setText("油品汇总");
        } else if (car.orderType == 2){
            tvOrderType.setVisibility(View.VISIBLE);
            tvOrderType.setText("非油品汇总");
        }
        tvType.setText(car.title);
        tvPrice.setText(car.now_price);
        tvPast.setText("原价:"+car.past_price);
        tvNum.setText("x"+car.num);
        editText.setText(car.num+"");
        x.image().bind(ivPic,car.pic, BitmapUtil.getOptionCommon());
        layout.addView(view);
    }
    @Override
    public void setMoney(String money) {
        tvMoney.setText(money);
    }
    @Override
    public void setToPay(String pay) {
        tvToPay.setText(pay);
    }
    @Override
    public void toPayView(OrderDetail order) {
        Intent intentBuy = new Intent(getContext(), OrderActivity.class);
        intentBuy.putExtra(CommonCode.INTENT_ORDER_ID, order.oid);
        startActivity(intentBuy);
        getActivity().finish();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_titlebar_right:
                break;
            case R.id.tv_shoppingcar_topay:
                presenter.payShoppingCar();
                break;
        }
    }
}