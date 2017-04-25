package cn.linxi.iu.com.presenter.ipresenter;
import android.widget.EditText;
/**
 * Created by buzhiheng on 2017/3/13.
 */
public interface IAutomacDetailPresenter {
    void getAutomacDetail();
    void setCoutAdd(EditText editText);
    void setCoutSub(EditText editText);
    void addShoppingCar(EditText editText);
    void order(EditText etCout);
}