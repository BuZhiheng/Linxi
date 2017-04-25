package cn.linxi.iu.com.view.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.xutils.x;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.AutomacDetail;
import cn.linxi.iu.com.model.AutomacDetailFormat;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.OrderDetail;
import cn.linxi.iu.com.presenter.AutomacDetailPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IAutomacDetailPresenter;
import cn.linxi.iu.com.util.BitmapUtil;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IAutomacDetailView;
/**
 * Created by buzhiheng on 2017/3/13.
 */
public class AutomacDetailActivity extends AppCompatActivity implements IAutomacDetailView, View.OnClickListener{
    private IAutomacDetailPresenter presenter;
    @Bind(R.id.iv_automac_detail_pic)
    ImageView ivPic;
    @Bind(R.id.tv_automac_detail_title)
    TextView tvTitle;
    @Bind(R.id.tv_automac_detail_price)
    TextView tvPrice;
    @Bind(R.id.tv_automac_detail_stock)
    TextView tvStock;
    @Bind(R.id.et_automac_detail_cout)
    EditText etCout;
//    @Bind(R.id.banner_automac_detail)
//    ConvenientBanner banner;
    @Bind(R.id.ll_automac_detail_format)
    LinearLayout llFormat;
//    private List<AutomacBanner> banners = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automac_detail);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("商品详情");
        ((ImageView)findViewById(R.id.iv_titlebar_right)).setImageResource(R.drawable.ic_shopping_car);
//        banner.setPages(new CBViewHolderCreator<AutomacBannerViewHolder>() {
//            @Override
//            public AutomacBannerViewHolder createHolder() {
//                return new AutomacBannerViewHolder(banners);
//            }
//        },banners).setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
//        banner.startTurning(5000);
        presenter = new AutomacDetailPresenter(this, getIntent());
        presenter.getAutomacDetail();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.iv_titlebar_right:
                Intent intent = new Intent(this,ShoppingCarFrmActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_automac_detail_add:
                presenter.setCoutAdd(etCout);
                break;
            case R.id.iv_automac_detail_sub:
                presenter.setCoutSub(etCout);
                break;
            case R.id.iv_automac_detail_clear:
                etCout.setText("");
                break;
            case R.id.tv_automac_detail_add:
                presenter.addShoppingCar(etCout);
                break;
            case R.id.tv_automac_detail_buy:
                presenter.order(etCout);
                break;
        }
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void setAutomacDetail(AutomacDetail detail) {
        tvTitle.setText(detail.title);
        tvPrice.setText(detail.now_price);
        tvStock.setText("库存:"+detail.stock);
        x.image().bind(ivPic, detail.pic, BitmapUtil.getOptionCommon());
    }
    @Override
    public void setCout(String cout) {
        etCout.setText(cout);
    }
    @Override
    public void setFormat(AutomacDetailFormat format) {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_automac_format,null);
        TextView tv = (TextView) view.findViewById(R.id.tv_automac_detail_format);
        tv.setText(format.key+": "+format.value);
        llFormat.addView(view);
    }
    @Override
    public void toPayView(OrderDetail order) {
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(CommonCode.INTENT_ORDER_ID,order.oid);
        startActivity(intent);
        finish();
    }
}