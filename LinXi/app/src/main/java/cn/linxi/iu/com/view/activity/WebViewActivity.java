package cn.linxi.iu.com.view.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.StringUtil;
/**
 * Created by buzhiheng on 2016/10/12.
 */
public class WebViewActivity extends AppCompatActivity {
    @Bind(R.id.web_common)
    WebView webView;
    private String rule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        Intent intent = getIntent();
        if (intent != null){
            String webUrl = intent.getStringExtra(CommonCode.INTENT_WEBVIEW_URL);
            String s = intent.getStringExtra(CommonCode.INTENT_COMMON);
            rule = intent.getStringExtra(CommonCode.SP_USER_GAME_RULE);
            String title = s==null?"":s;
            ((TextView)findViewById(R.id.tv_titlebar_title)).setText(title);
            if (!StringUtil.isNull(rule)){
                ((TextView)findViewById(R.id.tv_titlebar_right)).setText("游戏规则");
            }
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                }
            });//播放视频
            webView.loadUrl(webUrl);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                }
            });
        }
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.tv_titlebar_right:
                Intent intent = new Intent(this,WebViewActivity.class);
                intent.putExtra(CommonCode.INTENT_WEBVIEW_URL, rule);
                intent.putExtra(CommonCode.INTENT_COMMON,"游戏规则");
                startActivity(intent);
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            //停止播放音乐,视频等
            webView.getClass().getMethod("onPause").invoke(webView, (Object[]) null);
            webView.removeAllViews();
            webView.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webView != null && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}