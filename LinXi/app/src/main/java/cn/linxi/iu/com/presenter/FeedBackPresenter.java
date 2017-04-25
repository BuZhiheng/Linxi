package cn.linxi.iu.com.presenter;

import android.widget.EditText;

import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IFeedBackPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IFeedBackView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;

/**
 * Created by buzhiheng on 2016/8/22.
 */
public class FeedBackPresenter implements IFeedBackPresenter {
    private IFeedBackView view;
    public FeedBackPresenter(IFeedBackView view){
        this.view = view;
    }
    @Override
    public void feedBack(EditText editText) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String content = editText.getText().toString();
        if (StringUtil.isNull(content)){
            view.showToast("请输入内容");
            return;
        }
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id",userId)
                .add("content", content)
                .build();
        OkHttpUtil.post(HttpUrl.feedBack, body, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                BaseResult result = GsonUtil.jsonToObject(s,BaseResult.class);
                if (result.success()){
                    view.showToast(result.error);
                    view.feedBackSuccess();
                }
            }
        });
    }
}
