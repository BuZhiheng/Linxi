package cn.linxi.iu.com.view.widget;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.linxi.iu.com.R;
public class PayPasswordView implements View.OnClickListener{
    private TextView box1;
    private TextView box2;
    private TextView box3;
    private TextView box4;
    private TextView box5;
    private TextView box6;
    private ArrayList<String> mList=new ArrayList<>();
    private View mView;
    private OnPayListener listener;
    public PayPasswordView(Context mContext,OnPayListener listener){
        getDecorView(mContext, listener);
    }
    public static PayPasswordView getInstance(Context mContext,OnPayListener listener){
        return new PayPasswordView(mContext,listener);
    }
    public void getDecorView(Context mContext,OnPayListener listener){
        this.listener=listener;
        mView=LayoutInflater.from(mContext).inflate(R.layout.dialog_pay_password, null);
        box1 = (TextView) mView.findViewById(R.id.tv_dialog_psd1);
        box2 = (TextView) mView.findViewById(R.id.tv_dialog_psd2);
        box3 = (TextView) mView.findViewById(R.id.tv_dialog_psd3);
        box4 = (TextView) mView.findViewById(R.id.tv_dialog_psd4);
        box5 = (TextView) mView.findViewById(R.id.tv_dialog_psd5);
        box6 = (TextView) mView.findViewById(R.id.tv_dialog_psd6);
        mView.findViewById(R.id.tv_dialog_keyboard0).setOnClickListener(this);
        mView.findViewById(R.id.tv_dialog_keyboard1).setOnClickListener(this);
        mView.findViewById(R.id.tv_dialog_keyboard2).setOnClickListener(this);
        mView.findViewById(R.id.tv_dialog_keyboard3).setOnClickListener(this);
        mView.findViewById(R.id.tv_dialog_keyboard4).setOnClickListener(this);
        mView.findViewById(R.id.tv_dialog_keyboard5).setOnClickListener(this);
        mView.findViewById(R.id.tv_dialog_keyboard6).setOnClickListener(this);
        mView.findViewById(R.id.tv_dialog_keyboard7).setOnClickListener(this);
        mView.findViewById(R.id.tv_dialog_keyboard8).setOnClickListener(this);
        mView.findViewById(R.id.tv_dialog_keyboard9).setOnClickListener(this);
        mView.findViewById(R.id.tv_dialog_keyboarddel).setOnClickListener(this);
        mView.findViewById(R.id.iv_paydialog_back).setOnClickListener(this);
        mView.findViewById(R.id.tv_dialog_forgetpsd).setOnClickListener(this);
    }
    public void onClick(View v){
        if(v.getId()==R.id.tv_dialog_keyboard0){
            parseActionType(KeyboardEnum.zero);
        }else if(v.getId()==R.id.tv_dialog_keyboard1){
            parseActionType(KeyboardEnum.one);
        }else if(v.getId()==R.id.tv_dialog_keyboard2){
            parseActionType(KeyboardEnum.two);
        }else if(v.getId()==R.id.tv_dialog_keyboard3){
            parseActionType(KeyboardEnum.three);
        }else if(v.getId()==R.id.tv_dialog_keyboard4){
            parseActionType(KeyboardEnum.four);
        }else if(v.getId()==R.id.tv_dialog_keyboard5){
            parseActionType(KeyboardEnum.five);
        }else if(v.getId()==R.id.tv_dialog_keyboard6){
            parseActionType(KeyboardEnum.sex);
        }else if(v.getId()==R.id.tv_dialog_keyboard7){
            parseActionType(KeyboardEnum.seven);
        }else if(v.getId()==R.id.tv_dialog_keyboard8){
            parseActionType(KeyboardEnum.eight);
        }else if(v.getId()==R.id.tv_dialog_keyboard9){
            parseActionType(KeyboardEnum.nine);
        }else if(v.getId()==R.id.tv_dialog_keyboarddel){
            parseActionType(KeyboardEnum.del);
        } else if (v.getId()==R.id.iv_paydialog_back){
            parseActionType(KeyboardEnum.cancel);
        } else if (v.getId()==R.id.tv_dialog_forgetpsd){
            parseActionType(KeyboardEnum.forget);
        }
    }
    private void parseActionType(KeyboardEnum type) {
        if(type.getType()== KeyboardEnum.ActionEnum.add){
            if(mList.size()<6){
                mList.add(type.getValue());
                updateUi();
                if (mList.size() == 6){
                    String payValue="";
                    for (int i = 0; i < mList.size(); i++) {
                        payValue+=mList.get(i);
                    }
                    listener.onSurePay(payValue);
                }
            }
        } else if(type.getType()== KeyboardEnum.ActionEnum.delete){
            if(mList.size()>0){
                mList.remove(mList.get(mList.size()-1));
                updateUi();
            }
        } else if(type.getType()== KeyboardEnum.ActionEnum.cancel){
            listener.onCancelPay();
        } else if(type.getType()== KeyboardEnum.ActionEnum.forget){
            listener.onForgetPayPsd();
        }
    }
    private void updateUi() {
        if(mList.size()==0){
            box1.setText("");
            box2.setText("");
            box3.setText("");
            box4.setText("");
            box5.setText("");
            box6.setText("");
        }else if(mList.size()==1){
            box1.setText(mList.get(0));
            box2.setText("");
            box3.setText("");
            box4.setText("");
            box5.setText("");
            box6.setText("");
        }else if(mList.size()==2){
            box1.setText(mList.get(0));
            box2.setText(mList.get(1));
            box3.setText("");
            box4.setText("");
            box5.setText("");
            box6.setText("");
        }else if(mList.size()==3){
            box1.setText(mList.get(0));
            box2.setText(mList.get(1));
            box3.setText(mList.get(2));
            box4.setText("");
            box5.setText("");
            box6.setText("");
        }else if(mList.size()==4){
            box1.setText(mList.get(0));
            box2.setText(mList.get(1));
            box3.setText(mList.get(2));
            box4.setText(mList.get(3));
            box5.setText("");
            box6.setText("");
        }else if(mList.size()==5){
            box1.setText(mList.get(0));
            box2.setText(mList.get(1));
            box3.setText(mList.get(2));
            box4.setText(mList.get(3));
            box5.setText(mList.get(4));
            box6.setText("");
        }else if(mList.size()==6){
            box1.setText(mList.get(0));
            box2.setText(mList.get(1));
            box3.setText(mList.get(2));
            box4.setText(mList.get(3));
            box5.setText(mList.get(4));
            box6.setText(mList.get(5));
        }
    }
    public interface OnPayListener{
        void onCancelPay();
        void onSurePay(String password);
        void onForgetPayPsd();
    }
    public View getView(){
        return mView;
    }
}