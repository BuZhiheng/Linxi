package cn.linxi.iu.com.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.bigkoo.convenientbanner.holder.Holder;
import org.xutils.x;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.BuyFrmBanner;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.BitmapUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.view.activity.WebViewActivity;
/**
 * Created by buzhiheng on 2016/12/1.
 */
public class BuyFrmBannerViewHolder implements Holder<BuyFrmBanner> {
    private View view;
    private FrameLayout fLayout;
    private ImageView iv;
    private TextView tv;
    private TextView tvIndex;
    private List<BuyFrmBanner> bannerList;
    public BuyFrmBannerViewHolder(List<BuyFrmBanner> bannerList){
        this.bannerList = bannerList;
    }
    @Override
    public View createView(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.activity_index_head,null);
        fLayout = (FrameLayout) view.findViewById(R.id.fl_indexfrm_head);
        iv = (ImageView) view.findViewById(R.id.iv_indexfrm_headview_photo);
        tv = (TextView) view.findViewById(R.id.tv_indexfrm_headview_title);
        tvIndex = (TextView) view.findViewById(R.id.tv_indexfrm_headview_index);
        return view;
    }
    @Override
    public void UpdateUI(final Context context, final int position, final BuyFrmBanner data) {
        final BuyFrmBanner banner = bannerList.get(position);
        x.image().bind(iv, banner.image, BitmapUtil.getOptionCommon());
        tvIndex.setText(position + 1 + "/" + bannerList.size());
        if (!StringUtil.isNull(banner.click_url)){
            fLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra(CommonCode.INTENT_WEBVIEW_URL,banner.click_url);
                    context.startActivity(intent);
                }
            });
        }
    }
}