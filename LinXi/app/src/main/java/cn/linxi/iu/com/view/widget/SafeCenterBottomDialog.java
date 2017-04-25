package cn.linxi.iu.com.view.widget;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;

import cn.linxi.iu.com.LXApplication;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.util.WindowUtil;

/**
 * Created by buzhiheng on 2016/8/10.
 */
public class SafeCenterBottomDialog extends Dialog {

    public SafeCenterBottomDialog(Context context) {
        super(context);
    }

    public SafeCenterBottomDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String sureButtonText;
        private String cancelButtonText;
        public Builder(Context context) {
            this.context = context;
        }
        public Builder setSureButton(String sureButtonText) {
            this.sureButtonText = sureButtonText;
            return this;
        }
        public Builder setCancelButton(String cancelButtonText) {
            this.cancelButtonText = cancelButtonText;
            return this;
        }
        public void create(final BottomDialogListener listener) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final SafeCenterBottomDialog dialog = new SafeCenterBottomDialog(context,R.style.CustomDialog);
            View view = inflater.inflate(R.layout.dialog_safecenter_bottom, null);

            Button btnSure = ((Button) view.findViewById(R.id.btn_dialog_safecenter_sure));
            Button btnCancel = ((Button) view.findViewById(R.id.btn_dialog_safecenter_cancel));
            // set the confirm button
            if (sureButtonText != null) {
                btnSure.setText(sureButtonText);
                if (listener != null) {
                    btnSure.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            listener.sureClick();
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                btnSure.setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (cancelButtonText != null) {
                btnCancel.setText(cancelButtonText);
                if (listener != null) {
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            listener.cancelClick();
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                btnCancel.setVisibility(
                        View.GONE);
            }
            dialog.setContentView(view);
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = WindowUtil.getWindowsWidth();
            params.gravity = Gravity.BOTTOM;
            dialog.getWindow().setAttributes(params);
            dialog.show();
        }
    }
    public interface BottomDialogListener{
        void sureClick();
        void cancelClick();
    }
}