package cn.linxi.iu.com;
import android.app.Application;
import android.content.Context;
import org.xutils.x;
import cn.linxi.iu.com.util.OkHttpUtil;
public class LXApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpUtil.initHttps(this);
        x.Ext.init(this);
        context = this;
    }
    public static Context getCtx(){
        return context;
    }
}