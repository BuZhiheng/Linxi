package cn.linxi.iu.com.util;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
/**
 * Created by buzhiheng on 2017/1/3.
 */
public class NAVIUtil {
    public static void toNAVIActivity(Context context,Double lat,Double lng){
        if (lat == null || lng == null){
            return;
        }
        if (SystemUtils.isAvilibleApk(SystemUtils.MAP_BAIDU)){
            Intent intent = new Intent();
            String latlon = lat+","+lng;
            intent.setData(Uri.parse("baidumap://map/navi?location=" + latlon));
            context.startActivity(intent);
            return;
        }
        double[] d = SystemUtils.bd09_To_Gcj02(lat,lng);
        if (SystemUtils.isAvilibleApk(SystemUtils.MAP_ALI)){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            //将功能Scheme以URI的方式传入data
            Uri uri = Uri.parse("androidamap://navi?sourceApplication=爱加油&lat="+d[0]+"&lon="+d[1]+"&dev=0&style=2");
            intent.setData(uri);
            //启动该页面即可
            context.startActivity(intent);
            return;
        }
        String latlon = d[0]+","+d[1];
        try {
            Uri mUri = Uri.parse("geo:" + latlon + "?q=" + "加油站");
            Intent intent = new Intent(Intent.ACTION_VIEW, mUri);
            context.startActivity(intent);
        } catch (Exception e) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q="+latlon));
            context.startActivity(i);
        }
    }
}