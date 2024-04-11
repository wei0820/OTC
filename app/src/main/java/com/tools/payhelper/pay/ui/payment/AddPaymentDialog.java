package com.tools.payhelper.pay.ui.payment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.jingyu.pay.ui.bankcard.BankCardDateModel;
import com.jingyu.pay.ui.bankcard.PaymentDateModel;
import com.tools.payhelper.R;
import com.tools.payhelper.pay.PayHelperUtils;
import com.tools.payhelper.pay.ToastManager;
import com.tools.payhelper.pay.ui.bankcard.AddBankCardData;
import com.tools.payhelper.pay.ui.bankcard.BanCardListData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AddPaymentDialog extends AlertDialog {
    private Activity activity;
    private EditText pd, name,tel,googleedt,eusername,payedt;
    private OnAddCallback onAddCallback;
    private OnAddBanKListCallback onAddBanKListCallback;
    private Handler handlerLoading = new Handler();
    private TextView nameedt;
    BankCardDateModel bankCardDateModel = new BankCardDateModel();
    PaymentDateModel paymentDateModel = new PaymentDateModel();
    private Spinner spinner;
    ArrayList<BanCardListData.Data> banklist = new ArrayList<>();
    String _bankname = "";
    String _subName = "";
    String _cardId = "";
    String _userName = "";
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

    public AddPaymentDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }


    protected AddPaymentDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected AddPaymentDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        View view = LayoutInflater.from(activity).inflate(R.layout.add_pay_ment, null);
        setView(view);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        spinner = findViewById(R.id.spinner);
        googleedt = findViewById(R.id.googleedt);
        nameedt = findViewById(R.id.nameedt);
        payedt = findViewById(R.id.payedt);

        Long tsLong = System.currentTimeMillis();
        String ts = tsLong.toString();

        nameedt.setText(ts);
        banklist = PayHelperUtils.getALLBankCardData(activity);

        MyAdapter adapter = new MyAdapter(banklist);
        // apply the Adapter:
        spinner.setAdapter(adapter);
        // onClickListener:
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item was selected (in the Spinner)
             */
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                BanCardListData.Data g = ( BanCardListData.Data) parent.getItemAtPosition(pos);
//                Toast.makeText(
//                        activity,
//                        g.userName+" is "+g.bankName,
//                        Toast.LENGTH_LONG).show();
                _bankname = g.bankName;
                _subName = g.subName;
                _cardId = g.cardNo;
                _userName = g.userName;

            }

            public void onNothingSelected(AdapterView parent) {
                // Do nothing.
            }
        });


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
//                String n = "支付宝转账";
//                String p = pd.getText().toString();
//                String t = "NA";
//                String google = googleedt.getText().toString();
//                String username = usernaem.getText().toString();
//                String euserName = eusername.getText().toString();
//                String pay = payedt.getText().toString().isEmpty() ?"50000" : payedt.getText().toString();
//                Float payF = Float.parseFloat(pay);
//                bankCardDateModel.setBankCard(activity, n, p, t, payF, google, username, euserName, new BankCardDateModel.BankCardResponse() {
//                    @Override
//                    public void getResponse(@NonNull String s) {
//                        if (!s.isEmpty()){
//                            AddBankCardData addBankCardData = new Gson().fromJson(s,AddBankCardData.class);
//                            if (addBankCardData !=null){
//                                onAddBanKListCallback.onResponse(addBankCardData);
//
//                            }
//                        }
//                    }
//                });

                String _orderNo = nameedt.getText().toString();
                String code = googleedt.getText().toString();

                Float score  = Float.parseFloat(payedt.getText().toString());


                paymentDateModel.setPayment(activity, _bankname, _subName, _orderNo, _cardId, code, _userName, score, new PaymentDateModel.BankCardResponse() {
                    @Override
                    public void getResponse(@NonNull String s) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("jack",s);
                                ToastManager.showToastCenter(activity,s);
                            }
                        });

                    }
                });

                dismiss();

            }
        });
    }

    private class MyAdapter extends BaseAdapter implements SpinnerAdapter {

        /**
         * The internal data (the ArrayList with the Objects).
         */
        private final ArrayList<BanCardListData.Data> data;

        public MyAdapter( ArrayList<BanCardListData.Data> data){
            this.data = data;
        }

        /**
         * Returns the Size of the ArrayList
         */
        @Override
        public int getCount() {
            return data.size();
        }

        /**
         * Returns one Element of the ArrayList
         * at the specified position.
         */
        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
        /**
         * Returns the View that is shown when a element was
         * selected.
         */
        @Override
        public View getView(int position, View recycle, ViewGroup parent) {
            TextView text;
            if (recycle != null){
                // Re-use the recycled view here!
                text = (TextView) recycle;
            } else {
                // No recycled view, inflate the "original" from the platform:
                text = (TextView) getLayoutInflater().inflate(
                        android.R.layout.simple_dropdown_item_1line, parent, false
                );
            }
            text.setText(data.get(position).cardNo);
            return text;
        }


    }
}
