package cn.linxi.iu.com.view.iview;
/**
 * Created by buzhiheng on 2016/10/26.
 */
public interface IBusinessPreSaleView {
    void refreshCodeButton(String time);
    void setCodeBtnCanClick();
    void setCodeBtnCanNotClick();
    void showToast(String toast);
    void toPreOilView(String identify);
    void dismissCardPop();
    void setCardPlate(String plate);
}