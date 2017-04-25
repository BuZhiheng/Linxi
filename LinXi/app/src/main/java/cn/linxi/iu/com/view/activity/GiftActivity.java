package cn.linxi.iu.com.view.activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.presenter.GiftPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IGiftPresenter;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IGiftView;
import cn.linxi.iu.com.view.widget.MyDialog;
/**
 * Created by buzhiheng on 2016/12/21.
 */
public class GiftActivity extends AppCompatActivity implements IGiftView{
    private IGiftPresenter presenter;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);
        initView();
    }
    private void initView() {
        presenter = new GiftPresenter(this);
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("夺宝奇兵");
        dialog = MyDialog.getNoticeDialog(this,"请稍后...");
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.ll_gift_envelop:
                dialog.show();
                presenter.getEnvelop();
                break;
            case R.id.ll_gift_prize:
                dialog.show();
                presenter.getPrize();
                break;
            case R.id.ll_gift_onebuy:
                showToast("正在玩命开发中,敬请期待...");
                break;
        }
    }
    @Override
    public void showToast(String toast) {
        dialog.dismiss();
        ToastUtil.show(toast);
    }
    @Override
    public void toWebView(String title, String url, String rule) {
        dialog.dismiss();
        Intent intent = new Intent(this,WebViewActivity.class);
        intent.putExtra(CommonCode.INTENT_WEBVIEW_URL,url);
        intent.putExtra(CommonCode.INTENT_COMMON,title);
        intent.putExtra(CommonCode.SP_USER_GAME_RULE, rule);
        startActivity(intent);
    }
}