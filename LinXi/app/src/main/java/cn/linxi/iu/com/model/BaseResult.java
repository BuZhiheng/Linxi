package cn.linxi.iu.com.model;
import com.google.gson.JsonElement;
import org.greenrobot.eventbus.EventBus;
/**
 * Created by buzhiheng on 2016/8/17.
 *
 * Desc 服务器返回总json构造类, err code data
 */
public class BaseResult {
    public Integer code;
    public String error;
    public Data data;
    public boolean success(){
        if (code != null && code == 200){
            return true;
        } else if (code != null && code == 400){
            //重新登录
//            ToastUtil.show("400");
//            EventBus.getDefault().post(new EventRelogin());
            return false;
        }
        return false;
    }
    public class Data {
        public JsonElement result;
        public JsonElement banner;
        public Float balance;
    }
    public String getResult(){
        if (data != null && data.result != null){
            return data.result.toString();
        }
        return "";
    }
}