package cn.linxi.iu.com.view.iview;
import java.util.List;

import cn.linxi.iu.com.model.BusinessIndexData;
import cn.linxi.iu.com.model.BusinessIndexDataItem;
/**
 * Created by buzhiheng on 2017/4/14.
 */
public interface IBusinessIndexView {
    void showToast(String toast);
    void setData(BusinessIndexData data);
    void setDataItem(List<BusinessIndexDataItem> list);
    void setStime(String time);
    void setEtime(String time);
}