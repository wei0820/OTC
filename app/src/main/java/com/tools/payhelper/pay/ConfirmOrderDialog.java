package com.tools.payhelper.pay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.jingyu.pay.ui.dashboard.SellDateModel;
import com.tools.payhelper.R;
import com.tools.payhelper.pay.ui.dashboard.ConfirmData;
import com.tools.payhelper.pay.ui.dashboard.SellListData;


public class ConfirmOrderDialog extends AlertDialog {
    private Activity activity;
    private EditText max, name;
    private OnAddCallback onAddCallback;
    private OnAddBanKListCallback onAddBanKListCallback;
    private Dialog dialog;
    private SellListData.Data mSellListData;

    private SellDateModel sellDateModel = new SellDateModel();
    private Handler handlerLoading = new Handler();

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
        void onResponse(ConfirmData confirmData);
    }

    public ConfirmOrderDialog(Activity activity, SellListData.Data sellListData) {
        super(activity);
        this.activity = activity;
        this.mSellListData = sellListData;
    }


    protected ConfirmOrderDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected ConfirmOrderDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_sell_sure, null);
        setView(view);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        name = findViewById(R.id.bank_card);
        max = findViewById(R.id.bank_card_no);
        String maxString = mSellListData.score+"";
        String minString = mSellListData.userName;
        String ischeckString =PayHelperUtils.getBuyIsOpen(activity) ? "买币已开启" : "买币已关闭";

        boolean ischeck = PayHelperUtils.getBuyIsOpen(activity);
        name.setHint(minString);
        max.setHint(maxString);



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
                String _name = name.getText().toString().isEmpty() ? mSellListData.userName + "" : name.getText().toString();
//                String id = max.getText().toString().isEmpty() ? mSellListData.userName: max.getText().toString();

                sellDateModel.setConfirmOrder(mSellListData.id, _name, activity, new SellDateModel.SellResponse() {
                    @Override
                    public void getResponse(@NonNull String s) {

                        if (!s.isEmpty()){
                            ConfirmData confirmData = new Gson().fromJson(s,ConfirmData.class);
                            if (confirmData!=null){
                                onAddBanKListCallback.onResponse(confirmData);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastManager.showToastCenter(activity,confirmData.msg);
                                        dismiss();
                                    }
                                });

                            }
                        }
                    }
                });


            }
        });
    }

}
