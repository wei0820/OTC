package com.tools.payhelper.pay.ui.money

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.ClipboardManager
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jingyu.pay.ui.notifications.TransferMoneyViewModel
import com.jingyu.pay.ui.notifications.TransferMoneyViewModelFactory
import com.squareup.picasso.Picasso
import com.tools.payhelper.R
import com.tools.payhelper.pay.Constant
import com.tools.payhelper.pay.ToastManager

class BuyumoneyActivity : AppCompatActivity() {
    lateinit var  mCopyTextView: TextView
    lateinit var okBtn : Button
    lateinit var closeBtn : Button
    val transferMoneyViewModel: TransferMoneyViewModel by lazy {
        ViewModelProvider(this, TransferMoneyViewModelFactory()).get(TransferMoneyViewModel::class.java)
    }
    lateinit var  transEdit :EditText
    lateinit var scroeEditText: EditText
    lateinit var mGoogleEditText: EditText
    lateinit var qrimg : ImageView
    lateinit var mMonytttext : TextView
    lateinit var copybtn : Button
    lateinit var mHashvalueEditText: EditText
    var copytextt = ""
    var pprice = 0.0
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.setBackgroundDrawableResource(android.R.color.transparent)
//        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        setContentView(R.layout.buy_money)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        checkUserinfo()
        mCopyTextView = findViewById(R.id.copytext)
        mMonytttext = findViewById(R.id.monytttext)
        mHashvalueEditText = findViewById(R.id.hashvedt)
        qrimg = findViewById(R.id.qrimg);
        closeBtn = findViewById(R.id.closeBtn)
        copybtn = findViewById(R.id.copybtn)
        closeBtn.setOnClickListener {

            this.finish()
        }
        transferMoneyViewModel.getUSDTInfo(this).observe(this, Observer {
            if (it!=null){
                if (it.code ==0){
                    if (it.data!=null){
                        //api域名/[錢包址地].png
                        var url = Constant.API_URL+it.data.usdT_ADDR+".png";
                        Log.d("usdt",url);
                        Picasso.with(this).load(url).into(qrimg);
                        mCopyTextView.text = it.data.usdT_ADDR
                        copytextt =  it.data.usdT_ADDR
                        pprice = it.data.usdT_EXRATE
                    }
                }else{
                    AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("当前暂停功能")
                        .setPositiveButton("确定") { _, _ ->this.finish() }
                        .show()
                }

            }else{
                AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("当前暂停功能")
                    .setPositiveButton("确定") { _, _ ->this.finish() }
                    .show()
            }

        })
        copybtn.setOnClickListener {
            copyToClipboard(copytextt)
            ToastManager.showToastCenter(this, "复制到剪贴簿:"+copytextt)

        }

        // 讀取圖片
        okBtn = findViewById(R.id.okBtn)
        scroeEditText = findViewById(R.id.payedt)
        scroeEditText.addTextChangedListener {
            var price = pprice * (scroeEditText.text.toString().toDouble())
            mMonytttext.text = pprice.toString()+"\t"+"/"+"\t"+two(price.toString())

        }
        mGoogleEditText = findViewById(R.id.googleedt)
        okBtn.setOnClickListener {

            if (scroeEditText.text.isEmpty()){
                ToastManager.showToastCenter(this,"请勿输入空值")

                return@setOnClickListener}
            if (mGoogleEditText.text.isEmpty()){
                ToastManager.showToastCenter(this,"请勿输入空值")
                return@setOnClickListener}
            if (mHashvalueEditText.text.isEmpty()){
                ToastManager.showToastCenter(this,"请勿输入空值")
                return@setOnClickListener}

            var  scroe = scroeEditText.text.toString().toDouble()
            var  google = mGoogleEditText.text.toString()
            var hasvaule = mHashvalueEditText.text.toString()


            transferMoneyViewModel.setPostUsdtData(this,scroe,google,hasvaule).observe(this,
                Observer {
                    if (it!=null){
                        runOnUiThread {
                            Toast.makeText(this,it.msg,Toast.LENGTH_SHORT).show()
                            this.finish()
                        }
                    }
                })

        }

    }
    private fun copyToClipboard(str: String) {
        val sdk = Build.VERSION.SDK_INT
        if (sdk < Build.VERSION_CODES.HONEYCOMB) {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.text = str
        } else {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = ClipData.newPlainText("text label", str)
            clipboard.setPrimaryClip(clip)
        }
    }
    fun two(s: String): String {
        return String.format("%.2f", s.toDouble())
    }

}