package cn.linxi.iu.com.presenter.ipresenter;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

/**
 * Created by buzhiheng on 2017/4/26.
 */
public interface IBusinessSaleOilPresenter {
    void getStationDetail(AppCompatActivity context);

    void onCarCardPopClick(EditText etPlate, String s);

    void onSubCarCardClick(EditText etPlate);
}