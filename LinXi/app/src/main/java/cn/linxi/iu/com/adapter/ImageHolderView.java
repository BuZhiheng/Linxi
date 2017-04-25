package cn.linxi.iu.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import cn.linxi.iu.com.R;
import cn.linxi.iu.com.util.BitmapUtil;

/**
 * Created by buzhiheng on 2016/7/19.
 */
public class ImageHolderView implements Holder<String> {
    private View view;
    private FrameLayout fLayout;
    private ImageView iv;
    private TextView tv;
    private TextView tvIndex;
    private ImageOptions optionHead;
    @Override
    public View createView(Context context) {
        optionHead = BitmapUtil.getOptionCommon();
        view = LayoutInflater.from(context).inflate(R.layout.activity_index_head,null);
        fLayout = (FrameLayout) view.findViewById(R.id.fl_indexfrm_head);
        iv = (ImageView) view.findViewById(R.id.iv_indexfrm_headview_photo);
        tv = (TextView) view.findViewById(R.id.tv_indexfrm_headview_title);
        tvIndex = (TextView) view.findViewById(R.id.tv_indexfrm_headview_index);
        return view;
    }
    @Override
    public void UpdateUI(final Context context, final int position, final String data) {
        x.image().bind(iv, data,optionHead);
    }
}