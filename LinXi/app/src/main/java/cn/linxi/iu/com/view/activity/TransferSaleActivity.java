package cn.linxi.iu.com.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import cn.linxi.iu.com.R;

/**
 * Created by buzhiheng on 2017/5/9.
 */
public class TransferSaleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_sale);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_titlebar_title)).setText("转让");
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
        }
    }
}