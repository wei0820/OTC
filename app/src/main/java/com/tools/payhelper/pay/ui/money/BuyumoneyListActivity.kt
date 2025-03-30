package com.tools.payhelper.pay.ui.money

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.tools.payhelper.pay.PayHelperUtils
import com.tools.payhelper.pay.ToastManager

class BuyumoneyListActivity : AppCompatActivity() {
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
        setContentView(R.layout.buy_money_list)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        checkUserinfo()


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
    fun confirmOrder(id : String){
        var url : String = PayHelperUtils.getOpenUrl(this) + "USDTUpload/" +id
        ToastManager.showToastCenter(this,url)
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(url)
        startActivity(intent)

        runOnUiThread {


        }


    }


}