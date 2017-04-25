package cn.linxi.iu.com.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.bigkoo.convenientbanner.holder.Holder;
import org.xutils.x;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.AutomacBanner;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.BitmapUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.view.activity.WebViewActivity;
/**
 * Created by buzhiheng on 2016/12/1.
 */
public class AutomacBannerViewHolder implements Holder<AutomacBanner> {
    private View view;
    private ImageView iv;
    private List<AutomacBanner> bannerList;
    public AutomacBannerViewHolder(List<AutomacBanner> bannerList){
        this.bannerList = bannerList;
    }
    @Override
    public View createView(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.activity_automac_banner,null);
        iv = (ImageView) view.findViewById(R.id.iv_automac_banner);
        return view;
    }
    @Override
    public void UpdateUI(final Context context, final int position, final AutomacBanner data) {
        final AutomacBanner banner = bannerList.get(position);
        x.image().bind(iv, banner.img, BitmapUtil.getOptionCommon());
        if (!StringUtil.isNull(banner.href)){
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra(CommonCode.INTENT_WEBVIEW_URL,banner.href);
                    context.startActivity(intent);
                }
            });
        }
    }
}