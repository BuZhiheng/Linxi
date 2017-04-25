package cn.linxi.iu.com.presenter.ipresenter;
import android.widget.EditText;
/**
 * Created by buzhiheng on 2016/10/26.
 */
public interface IBusinessPreSalePresenter {
    void getCode(EditText etPhone);
    void saleCheckCode(EditText etPhone,EditText etCode);
    void sale(String province, EditText identify);
    void onCarCardPopClick(EditText plate,String s);
    void onSubCarCardClick(EditText plate);
}