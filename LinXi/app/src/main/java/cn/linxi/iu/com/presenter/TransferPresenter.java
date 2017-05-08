package cn.linxi.iu.com.presenter;
import cn.linxi.iu.com.presenter.ipresenter.ITransferPresenter;
import cn.linxi.iu.com.view.iview.ITransferView;
/**
 * Created by buzhiheng on 2017/5/4.
 */
public class TransferPresenter implements ITransferPresenter {
    private ITransferView view;
    public TransferPresenter(ITransferView view){
        this.view = view;
    }
    @Override
    public void getData() {
    }
}