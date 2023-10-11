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
import com.tools.payhelper.pay.ToastManager;


public class AddGroupDialog extends AlertDialog {
    private Activity activity;
    private EditText pd, name,tel;
    private Spinner spinner;
    private OnAddCallback onAddCallback;
    private OnAddBanKListCallback onAddBanKListCallback;
    private Dialog dialog;
    private Switch aSwitch;
    private  EditText textView,textView2,textiew22;
    private Handler handlerLoading = new Handler();
    GroupDateModel groupDateModel = new GroupDateModel();
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
        void onResponse(RegisterData buyData);
    }

    public AddGroupDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }


    protected AddGroupDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected AddGroupDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_group, null);
        setView(view);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        name = findViewById(R.id.bank_card);
        pd = findViewById(R.id.bank_card_no);
        textView = findViewById(R.id.text1);
        textView2 = findViewById(R.id.text2);
        textiew22 = findViewById(R.id.text22);
        tel = findViewById(R.id.teledt);
        String maxString = PayHelperUtils.getRebate(activity).isEmpty() ? "" : PayHelperUtils.getRebate(activity);
        String minString = PayHelperUtils.getPaymentXeRebate(activity).isEmpty() ? "" : PayHelperUtils.getPaymentXeRebate(activity);
        String alipayRebate = PayHelperUtils.getAlipayRebate(activity).isEmpty()? "" : PayHelperUtils.getAlipayRebate(activity);




        TextView message = findViewById(R.id.message);
        message.setText("您的佣金比例:\n"+"卖币:"+maxString+"\n"+"小额买币:"+minString+"\n"+"支付宝卖币:"+alipayRebate);
        textView.setText(maxString);
        textView2.setText(minString);
        textiew22.setText(alipayRebate);


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
                String n = name.getText().toString();
                String p = pd.getText().toString();
                String t = tel.getText().toString();
                Double re = Double.parseDouble(textView.getText().toString());
                Double Pa = Double.parseDouble(textView2.getText().toString());
                Double aa = Double.parseDouble(textiew22.getText().toString());
                if (re>Double.parseDouble(maxString)){
                    ToastManager.showToastCenter(activity,"只能低于点位");

                }
                if (Pa>Double.parseDouble(minString)){
                    ToastManager.showToastCenter(activity,"只能低于点位");
                }
                if (aa>Double.parseDouble(alipayRebate)){
                    ToastManager.showToastCenter(activity,"只能低于点位");
                }

                groupDateModel.getGroupRegister(activity, n, p, t,
                        re,
                        Pa,
                        aa,
                        new GroupDateModel.GroupResponse() {
                            @Override
                            public void getResponse(@NonNull String s) {
                                if (!s.isEmpty()){
                                    RegisterData registerData = new Gson().fromJson(s,RegisterData.class);
                                    if (registerData!=null){
                                        onAddBanKListCallback.onResponse(registerData);
                                    }
                                }
                            }
                        });

                dismiss();

            }
        });
    }

}
