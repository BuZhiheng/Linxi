package cn.linxi.iu.com.model;

import com.google.gson.JsonElement;

import cn.linxi.iu.com.util.OkHttpUtil;
import okhttp3.RequestBody;
import rx.Subscriber;

/**
 * Created by buzhiheng on 2016/7/13.
 */
public class Result {
    private JsonElement result;
    private String reason;

    public JsonElement getResult() {
        return result;
    }

    public void setResult(JsonElement result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    public void getNews(String url, RequestBody requestBody, Subscriber<String> sub){
        OkHttpUtil.post(url, requestBody, sub);
    }
}
