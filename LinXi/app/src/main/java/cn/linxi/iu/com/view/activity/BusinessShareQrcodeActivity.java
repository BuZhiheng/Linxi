package cn.linxi.iu.com.view.activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/10/25.
 */
public class BusinessShareQrcodeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_shareqrcode);
        request();
    }
    private void request() {
        String operatId = PrefUtil.getInt(CommonCode.SP_USER_OPERA_ID, 0)+"";
        final String url = HttpUrl.businessShowQRCode+ OkHttpUtil.getSign()+"&operat_id="+operatId;
        OkHttpUtil.get(url, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
                Log.i(">>>>>>>>>", s);
            }
        });
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_presale_back:
                finish();
                break;
        }
    }
}