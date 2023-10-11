package com.tools.payhelper.pay.ui.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jingyu.pay.ui.bankcard.BankCardDateModel;
import com.jingyu.pay.ui.login.LoginDateModel;
import com.jingyu.pay.ui.login.LoginViewModel;
import com.tools.payhelper.R;
import com.tools.payhelper.pay.ToastManager;
import com.tools.payhelper.pay.ui.bankcard.AddBankCardData;


public class AddGoogleDialog extends AlertDialog {
    private Activity activity;
    private Spinner spinner;
    private OnAddCallback onAddCallback;
    private OnAddBanKListCallback onAddBanKListCallback;
    private Dialog dialog;
    private Switch aSwitch;
    private  TextView textView,textView2;
    private Handler handlerLoading = new Handler();

    private  EditText googleedt;
    private TextView googlemail,googletext;
    private ImageButton googlecopy;
    private ImageView googleimg;
    LoginDateModel loginDateModel = new LoginDateModel();

    public void setOnAddCallback(OnAddCallback onAddCallback) {
        this.onAddCallback = onAddCallback;
    }

    public  void  setAddBankCallback(OnAddBanKListCallback onAddBanKListCallback){
        this.onAddBanKListCallback = onAddBanKListCallback;

    }

    public interface OnAddCallback {
        void onAdd(String cardNo, String bankName, String bankCode);
    }
    public  interface  OnAddBanKListCallback{
        void onResponse(BindKeyData bindKeyData);
    }

    public AddGoogleDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }


    protected AddGoogleDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected AddGoogleDialog(Context context, int themeResId) {
        super(context, themeResId);
    }
    private  GoogleData googleData ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        View view = LayoutInflater.from(activity).inflate(R.layout.add_google, null);
        setView(view);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        googleimg = findViewById(R.id.googleimg);
        googleedt = findViewById(R.id.googleedt);
        googlemail = findViewById(R.id.googlemail);
        googletext = findViewById(R.id.googletext);
        googlecopy = findViewById(R.id.googlecopy);

        loginDateModel.getGoogle(activity,new LoginDateModel.LoginrResponse() {

            @Override
            public void getResponse(@NonNull String s) {
                if (!s.isEmpty()){
                    googleData = new Gson().fromJson(s,GoogleData.class);
                    if (googleData!=null){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                googlemail.setText(googleData.data.account);
                                googletext.setText(googleData.data.accountSecretKey);
                                Glide.with(activity).load(googleData.data.qrCodeSetupImageUrl).into(googleimg);
                                googlecopy.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        copyToClipboard(googleData.data.accountSecretKey);
                                        ToastManager.showToastCenter(activity,"复制到剪贴簿");
                                    }
                                });



                            }
                        });
                    }
                }
            }
        });




        view.findViewById(R.id.okBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (googleData!=null){
                    if (!googleedt.getText().toString().isEmpty()){
                        String code = googleedt.getText().toString();
                        setgooleKey(googleData.data.accountSecretKey,code);

                    }

                }


            }
        });
    }


    private void copyToClipboard(String str){
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(str);
            Log.e("version","1 version");
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager )activity.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("text label",str);
            clipboard.setPrimaryClip(clip);
            Log.e("version","2 version");
        }
    }

    public void setgooleKey(String key,String code){
        loginDateModel.setGoogleKey(activity,key, code, new LoginDateModel.LoginrResponse() {
            @Override
            public void getResponse(@NonNull String s) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!s.isEmpty()){
                            Log.d("Jack",s);
                            BindKeyData bindKeyData = new Gson().fromJson(s,BindKeyData.class);
                            if (bindKeyData!=null){
                                onAddBanKListCallback.onResponse(bindKeyData);
                            }
                        }
                    }
                });
            }
        });
    }
}
