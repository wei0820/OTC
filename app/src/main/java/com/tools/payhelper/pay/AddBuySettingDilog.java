package com.tools.payhelper.pay;

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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.google.gson.Gson;
import com.jingyu.pay.ui.home.HomeDateModel;
import com.tools.payhelper.R;
import com.tools.payhelper.pay.ui.home.StartBuyData;


public class AddBuySettingDilog extends AlertDialog {
    private Activity activity;
    private EditText max,min;
    private Spinner spinner;
    private OnAddCallback onAddCallback;
    private OnAddBanKListCallback onAddBanKListCallback;
    private Dialog dialog;
    private Switch aSwitch;

    private Handler handlerLoading = new Handler();
    HomeDateModel homeDateModel = new HomeDateModel();

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
        void onResponse(StartBuyData buyData);
    }

    public AddBuySettingDilog(Activity activity) {
        super(activity);
        this.activity = activity;
    }


    protected AddBuySettingDilog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected AddBuySettingDilog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_bank_card, null);
        setView(view);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        min = findViewById(R.id.bank_card);
        max = findViewById(R.id.bank_card_no);
        aSwitch = findViewById(R.id.switch1);
        String maxString = PayHelperUtils.getBuyMax(activity).isEmpty() ? "" : PayHelperUtils.getBuyMax(activity);
        String minString = PayHelperUtils.getBuyMin(activity).isEmpty() ? "" : PayHelperUtils.getBuyMin(activity);
        String ischeckString =PayHelperUtils.getBuyIsOpen(activity) ? "买币已开启" : "买币已关闭";

        boolean ischeck = PayHelperUtils.getBuyIsOpen(activity);
        min.setText(minString);
        max.setText(maxString);
        aSwitch.setChecked(ischeck);
        aSwitch.setText(ischeckString);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("Jack",b+"");
                String ischeckString = b ? "买币已开启" : "买币已关闭";
                aSwitch.setText(ischeckString);


                PayHelperUtils.saveBuyIsOpen(activity,b);

            }
        });



        TextView message = findViewById(R.id.message);
//        message.setText(Constant.getEnvironmentInfo().getMessage());


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
                String minSt = min.getText().toString().isEmpty() ? "300" : min.getText().toString();
                String maxSt = max.getText().toString().isEmpty() ? "1000": max.getText().toString();

                PayHelperUtils.saveBuyMax(activity,maxSt);
                PayHelperUtils.saveBuyMin(activity,minSt);



                homeDateModel.setBuySetting(activity, minSt, maxSt, new HomeDateModel.BuyResponse() {
                    @Override
                    public void getResponse(@NonNull String s) {
                        if (!s.isEmpty()){
                            StartBuyData buyData = new Gson().fromJson(s, StartBuyData.class);
                            if (buyData!=null){
                                onAddBanKListCallback.onResponse(buyData);
                                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                                if (inputMethodManager != null) {
                                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }
                            }
                        }

                    }

                    @Override
                    public void getFailure(@NonNull String s) {

                    }
                });


            }
        });
    }

}
