package com.tools.payhelper.pay.ui.group;

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
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.jingyu.pay.ui.group.GroupDateModel;
import com.tools.payhelper.R;
import com.tools.payhelper.pay.PayHelperUtils;


public class AddAllDataDialog extends AlertDialog {
    private Activity activity;
    private Spinner spinner;
    private OnAddCallback onAddCallback;
    private OnAddBanKListCallback onAddBanKListCallback;
    private Dialog dialog;
    private Switch aSwitch;
    private Handler handlerLoading = new Handler();
    private TextView titletxt,payment,collection,commission,paymentXe,paymentXeQty,paymentXeCom;
    GroupDateModel groupDateModel = new GroupDateModel();
    private String titletxtString,paymentString,collectionString,commissionString,paymentXeString,paymentXeQtyString,paymentXeComString;

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
        void onResponse(boolean b);
    }

    public AddAllDataDialog(Activity activity,String title,String payment,String collection,String commission,String paymentXe,String paymentXeQty) {
        super(activity);
        this.activity = activity;
        this.titletxtString = title;
        this.paymentString = payment;
        this.collectionString = collection;
        this.commissionString = commission;
        this.paymentXeString = paymentXe;
        this.paymentXeQtyString = paymentXeQty;
    }


    protected AddAllDataDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected AddAllDataDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_alldata, null);
        setView(view);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        titletxt = findViewById(R.id.titletxt);
        payment = findViewById(R.id.payment);
        collection = findViewById(R.id.collection);
        commission = findViewById(R.id.commission);
        paymentXe = findViewById(R.id.paymentXe);
        paymentXeQty = findViewById(R.id.paymentXeQty);
        paymentXeCom = findViewById(R.id.paymentXeCom);
        titletxt.setText(titletxtString);
        payment.setText("买币交易量"+paymentString);
        collection.setText("卖币交易量"+collectionString);
        commission.setText("佣金"+commissionString);
//        paymentXe.setText("小额买币交易量"+paymentXeString);
//        paymentXeQty.setText("小额买币笔数"+paymentXeQtyString);
//        paymentXeCom.setText("小额买币佣金"+paymentXeComString);


        view.findViewById(R.id.closeBtn).setOnClickListener(v -> {
            view.setEnabled(false);
            new Handler().postDelayed(() -> view.setEnabled(true), 500);

            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            onAddBanKListCallback.onResponse(true);
            dismiss();
        });

    }

}
