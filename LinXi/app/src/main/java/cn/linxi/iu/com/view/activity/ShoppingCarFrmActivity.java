package cn.linxi.iu.com.view.activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.presenter.ShoppingCarEditPresenter;
import cn.linxi.iu.com.view.fragment.ShoppingCarEditFragment;
import cn.linxi.iu.com.view.fragment.ShoppingCarFragment;
/**
 * Created by buzhiheng on 2017/3/21.
 */
public class ShoppingCarFrmActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.tv_titlebar_right)
    TextView tvRight;
    private ShoppingCarFragment fragmentCar;
    private ShoppingCarEditFragment fragmentEditCar;
    private FragmentManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_carfrm);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        tvRight.setText("编辑");
        tvRight.setOnClickListener(this);
        manager = getSupportFragmentManager();
        fragmentCar = new ShoppingCarFragment();
        fragmentEditCar = new ShoppingCarEditFragment();
        FragmentTransaction t = manager.beginTransaction();
        t.replace(R.id.ll_shoppingcar_content,fragmentCar).commit();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_titlebar_right:
                if (tvRight.getText().toString().equals("编辑")){
                    tvRight.setText("完成");
                    FragmentTransaction t = manager.beginTransaction();
                    t.replace(R.id.ll_shoppingcar_content,fragmentEditCar).commit();
                } else {
                    fragmentEditCar.updateShopping(new ShoppingCarEditPresenter.OnUpdateListner() {
                        @Override
                        public void success() {
                            tvRight.setText("编辑");
                            FragmentTransaction t = manager.beginTransaction();
                            t.replace(R.id.ll_shoppingcar_content, fragmentCar).commit();
                        }
                    });
                }
                break;
            case R.id.fl_titlebar_back:
                finish();
                break;
        }
    }
}