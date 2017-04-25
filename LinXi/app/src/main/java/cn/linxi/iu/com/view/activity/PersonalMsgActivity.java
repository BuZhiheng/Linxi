package cn.linxi.iu.com.view.activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.tencent.TIMManager;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;
import org.greenrobot.eventbus.EventBus;
import org.xutils.x;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.EventLogout;
import cn.linxi.iu.com.model.EventUserMsgChanged;
import cn.linxi.iu.com.presenter.PersonalPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IPersonalPresenter;
import cn.linxi.iu.com.util.BitmapUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.util.WindowUtil;
import cn.linxi.iu.com.view.iview.IPersonalView;
import cn.linxi.iu.com.view.widget.MyDialog;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
/**
 * Created by buzhiheng on 2016/8/8.
 */
public class PersonalMsgActivity extends AppCompatActivity implements IPersonalView{
    private IPersonalPresenter presenter;
    private final int HANDLE_UPLOAD_SUCCESS = 0X001;
    private final int HANDLE_UPLOAD_PROGRESS = 0x002;
    private String pro = "";
    private String photoUrl = "";//
    private ProgressDialog dialog;
    private AlertDialog exitDialog;
    @Bind(R.id.tv_personal_username)
    TextView tvUsername;
    @Bind(R.id.tv_personal_phone)
    TextView tvPhone;
    @Bind(R.id.tv_personal_invite_title)
    TextView tvInviteTitle;
    @Bind(R.id.tv_personal_invite_phone)
    TextView tvInvite;
    @Bind(R.id.iv_personalmsg_photo)
    ImageView ivPhoto;
    @Bind(R.id.iv_personal_vip)
    ImageView ivVip;
    private boolean invite = true;
    private Uri imageFilePath;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HANDLE_UPLOAD_SUCCESS:
                    dialog.dismiss();
                    ToastUtil.show("上传成功");
                    x.image().bind(ivPhoto, photoUrl, BitmapUtil.getOptionRadius(40));
                    break;
                case HANDLE_UPLOAD_PROGRESS:
                    dialog.setMessage(pro);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_msg);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        presenter = new PersonalPresenter(this);
        tvUsername.setText(PrefUtil.getString(CommonCode.SP_USER_VIPDESC,""));
        tvPhone.setText(PrefUtil.getString(CommonCode.SP_USER_PHONE, ""));
        x.image().bind(ivPhoto, PrefUtil.getString(CommonCode.SP_USER_PHOTO, ""), BitmapUtil.getOptionRadius(40));
        dialog = MyDialog.getNoticeDialog(this,"请稍后");
        dialog.setCancelable(true);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == BitmapUtil.PIC_PICTURE){//相册
                Bitmap b = BitmapUtil.getBitmapByPicture(this,data);
                if (b == null){
                    showToast("上传失败,去拍照试一下");
                    return;
                }
                presenter.upLoad(BitmapUtil.compressImage(this, b));
            } else if (requestCode == BitmapUtil.PIC_CAMERA){//相机
                int dp = WindowUtil.px2dip(this, 1000);
                BitmapUtil.cropImage(this, imageFilePath, dp, dp);
            } else if (requestCode == BitmapUtil.PIC_CROP){//裁剪
                Bitmap b = BitmapUtil.getBitmapByCameraOrCrop(data);
                presenter.upLoad(BitmapUtil.compressImage(this, b));
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        presenter.inviteMobile();
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_personalmsg_back:
                finish();
                break;
            case R.id.fl_personal_photo:
                CharSequence[] items = {"相册", "相机"};
                new AlertDialog.Builder(this)
                        .setTitle("上传头像")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    BitmapUtil.startPicture(PersonalMsgActivity.this);
                                } else {
                                    presenter.checkCameraPermission();
                                }
                            }
                        })
                        .create().show();
                break;
            case R.id.fl_personal_phone:
                if (StringUtil.isNull(PrefUtil.getString(CommonCode.SP_USER_PHONE, ""))){
                    Intent intentBind = new Intent(this, BindPhoneActivity.class);
                    startActivity(intentBind);
                    return;
                }
                Intent intentSafe = new Intent(this, SafeCenterActivity.class);
                startActivity(intentSafe);
                break;
            case R.id.fl_personal_invite:
                if (invite){
                    Intent intentInvite = new Intent(this, BindInviteActivity.class);
                    startActivity(intentInvite);
                } else {
                    showToast("您已经设置过推荐人啦");
                }
                break;
            case R.id.btn_personal_logout:
                exitDialog = MyDialog.getAlertDialog(this, "确定退出?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exitDialog.dismiss();
                        logout();
                    }
                });
                break;
        }
    }
    private void logout() {
        PrefUtil.putBoolean(CommonCode.SP_IS_LOGIN, false);
        PrefUtil.putBoolean(CommonCode.SP_IS_LOGIN_BUSINESS, false);
        PrefUtil.putBoolean(CommonCode.SP_IS_STARTED, true);
        Tencent.createInstance(CommonCode.APP_ID_QQ,this).logout(this);
        TIMManager.getInstance().logout();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        EventBus.getDefault().post(new EventLogout());
//        PrefUtil.putString(CommonCode.SP_USER_USERNAME, "");
//        PrefUtil.putString(CommonCode.SP_USER_PASSWORD,"");
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void showProdialog() {
        dialog.show();
    }
    @Override
    public void setVip(int vipIc,String vipDsc) {
        ivVip.setImageResource(vipIc);
    }
    @Override
    public void setProDialog(String msg) {
        pro = msg;
        handler.sendEmptyMessage(HANDLE_UPLOAD_PROGRESS);
    }
    @Override
    public void uploadSuccess(String url) {
        photoUrl = url;
        handler.sendEmptyMessage(HANDLE_UPLOAD_SUCCESS);
        EventBus.getDefault().post(new EventUserMsgChanged());
    }
    @Override
    public void setInviteMobile(String phone) {
        tvInviteTitle.setText("推荐人");
        tvInvite.setText(phone);
        invite = false;
    }
    @Override
    public void camera() {
        imageFilePath = BitmapUtil.startCamera(PersonalMsgActivity.this);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
    @PermissionSuccess(requestCode = 100)
    public void doSomething(){
        camera();
    }
    @PermissionFail(requestCode = 100)
    public void doFailSomething(){
//        showToast("授权失败");
    }
}