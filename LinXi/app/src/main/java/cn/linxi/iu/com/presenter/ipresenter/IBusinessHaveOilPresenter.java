package cn.linxi.iu.com.presenter.ipresenter;

import android.content.Intent;
import android.widget.EditText;

import cn.linxi.iu.com.model.CustomerOilCard;

/**
 * Created by buzhiheng on 2016/8/26.
 */
public interface IBusinessHaveOilPresenter {
    void getCustomerMsg(Intent intent);
    void order(CustomerOilCard selectCard,EditText purchase);
}