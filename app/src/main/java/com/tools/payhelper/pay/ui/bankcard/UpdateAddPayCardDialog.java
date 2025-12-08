package com.tools.payhelper.pay.ui.bankcard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.jingyu.pay.ui.bankcard.BankCardDateModel;
import com.tools.payhelper.R;


public class UpdateAddPayCardDialog extends AlertDialog {
    private Activity activity;
    private EditText pd, name,tel,googleedt,usernaem,eusername,payedt;
    private OnAddCallback onAddCallback;
    private OnAddBanKListCallback onAddBanKListCallback;
    private Handler handlerLoading = new Handler();
    BankCardDateModel bankCardDateModel = new BankCardDateModel();
    private Switch aSwitch;
    private boolean ischeck =false;
    private  BanCardListData.Data data;
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
        void onResponse(AddBankCardData addBankCardData);
    }

    public UpdateAddPayCardDialog(Activity activity,BanCardListData.Data data) {
        super(activity);
        this.activity = activity;
    }


    protected UpdateAddPayCardDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected UpdateAddPayCardDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        View view = LayoutInflater.from(activity).inflate(R.layout.add_pay_card, null);
        setView(view);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        name = findViewById(R.id.bank_card);
        pd = findViewById(R.id.bank_card_no);
        tel = findViewById(R.id.teledt);
        googleedt = findViewById(R.id.googleedt);
        usernaem = findViewById(R.id.nameedt);
        eusername = findViewById(R.id.enameedt);
        payedt = findViewById(R.id.payedt);
        aSwitch = findViewById(R.id.switchbutton);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    ischeck =b;
                }else {
                    ischeck =b;

                }
            }
        });
        if (data!=null){
            pd.setText(data.subName);
            usernaem.setText(data.userName);
            eusername.setText(data.pinYin);
            payedt.setText(String.valueOf(data.collectionlimit));
            aSwitch.setChecked(data.isAWXe);


        }


        view.findViewById(R.id.closeBtn).setOnClickListener(v -> {
            view.setEnabled(false);
            new Handler().postDelayed(() -> view.setEnabled(true), 500);

            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            dismiss();
        });

        view.findViewById(R.id.okBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = "支付宝转账";
                String p = pd.getText().toString();
                String t = "NA";
                String google = googleedt.getText().toString();
                String username = usernaem.getText().toString();
                String euserName = eusername.getText().toString();
                String pay = payedt.getText().toString().isEmpty() ?"50000" : payedt.getText().toString();
                Float payF = Float.parseFloat(pay);
                boolean b = ischeck;
                bankCardDateModel.setBankCard(activity, n, p, t, payF, google, username, euserName,b, data.id,data.isEnable,0,0,new BankCardDateModel.BankCardResponse() {
                    @Override
                    public void getResponse(@NonNull String s) {
                        if (!s.isEmpty()){
                            AddBankCardData addBankCardData = new Gson().fromJson(s,AddBankCardData.class);
                            if (addBankCardData !=null){
                                onAddBanKListCallback.onResponse(addBankCardData);

                            }
                        }
                    }
                });

                dismiss();

            }
        });
    }

}
