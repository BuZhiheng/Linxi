package cn.linxi.iu.com.presenter;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IPersonalPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PermissionUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IPersonalView;
import kr.co.namee.permissiongen.PermissionGen;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/9/7.
 */
public class PersonalPresenter implements IPersonalPresenter {
    private IPersonalView view;
    private Activity context;
    public PersonalPresenter(IPersonalView view){
        this.view = view;
        this.context = (Activity) view;
        int vip = PrefUtil.getInt(CommonCode.SP_USER_USER_IS_VIP, 0);
        if (vip == 0){
            view.setVip(R.drawable.ic_minefrm_vip0, "");
        } else if (vip == 1){
            view.setVip(R.drawable.ic_minefrm_vip1, "");
        } else if (vip == 2){
            view.setVip(R.drawable.ic_minefrm_vip2, "");
        } else if (vip == 3){
            view.setVip(R.drawable.ic_minefrm_vip3,"");
        }
    }
    @Override
    public void inviteMobile() {
        String invite = PrefUtil.getString(CommonCode.SP_USER_INVITE_MOBILE,"");
        if (!"".equals(invite)){
            //view.setInviteMobile(StringUtil.phoneSetMiddleGone(invite));
            view.setInviteMobile(invite);
        }
    }
    @Override
    public void upLoad(String path) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String endpoint = CommonCode.ALI_CLOUD_ENDPOINT;
        //明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的访问控制章节
        OSSCredentialProvider credentialProvider =
                new OSSPlainTextAKSKCredentialProvider(CommonCode.ALI_CLOUD_ACCESS_KEYID, CommonCode.ALI_CLOUD_ACCESS_KEYSECRET);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSS oss = new OSSClient(context, endpoint, credentialProvider, conf);
        //构造上传请求
//        String key = CommonCode.ALI_CLOUD_PHOTOURL+System.currentTimeMillis()+".jpg";
        String hash = System.currentTimeMillis()+""+((System.currentTimeMillis()+""+PrefUtil.getInt(CommonCode.SP_USER_USERID,0)).hashCode());
        final String key = "app/photo/photo"+hash+".jpg";
        PutObjectRequest put = new PutObjectRequest(CommonCode.ALI_CLOUD_BUCKETNAME, key, path);
        //异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                String pro = "当前:" + currentSize + " 总共:" + totalSize;
                view.setProDialog(pro);
            }
        });
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                uploadService("http://dev-oil.oss-cn-shanghai.aliyuncs.com/"+key);
            }
            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
        view.showProdialog();
    }

    @Override
    public void checkCameraPermission() {
        String permission = Manifest.permission.CAMERA;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtil.checkNoPermission(context,permission)) {
                if (PermissionUtil.checkDismissPermissionWindow(context,
                        permission)) {
                    view.showToast("权限被关闭,请打开相机权限");
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
                view.camera();
            }
        } else {
            view.camera();
        }
    }
    private void uploadService(final String url){
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id", userId)
                .add("avatar", url)
                .build();
        OkHttpUtil.post(HttpUrl.personalSetPhotoUrl, body, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
                BaseResult result = GsonUtil.jsonToObject(s,BaseResult.class);
                if (result.success()){
                    view.uploadSuccess(url);
                }
            }
        });
    }
}