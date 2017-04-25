package cn.linxi.iu.com.util;
import android.widget.Toast;

import cn.linxi.iu.com.LXApplication;

/**
 * Created by BuZhiheng on 2016/4/1.
 */
public class ToastUtil {
    public static void show(String msg){
        Toast.makeText(LXApplication.getCtx(), msg, Toast.LENGTH_SHORT).show();
    }
}