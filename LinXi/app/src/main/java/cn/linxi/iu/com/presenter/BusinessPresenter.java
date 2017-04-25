package cn.linxi.iu.com.presenter;
import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessPresenter;
import cn.linxi.iu.com.util.PermissionUtil;
import cn.linxi.iu.com.view.iview.IBusinessView;
import kr.co.namee.permissiongen.PermissionGen;
/**
 * Created by buzhiheng on 2016/12/3.
 */
public class BusinessPresenter implements IBusinessPresenter {
    private IBusinessView view;
    private AppCompatActivity context;
    public BusinessPresenter(IBusinessView view){
        this.view = view;
        context = (AppCompatActivity) view;
    }
    @Override
    public void checkPermissionCamera() {
        String permission = Manifest.permission.CAMERA;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtil.checkNoPermission(context, permission)) {
                if (PermissionUtil.checkDismissPermissionWindow(context,
                        permission)) {
                    view.showToast("请先为APP打开相机权限");
                    Intent intentSet = new Intent();
                    intentSet.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intentSet.setData(uri);
                    context.startActivity(intentSet);
                    return;
                }
                PermissionGen.with(context)
                        .addRequestCode(100)
                        .permissions(permission)
                        .request();
            } else {
                view.toQRScanView();
            }
        } else {
            view.toQRScanView();
        }
    }
}