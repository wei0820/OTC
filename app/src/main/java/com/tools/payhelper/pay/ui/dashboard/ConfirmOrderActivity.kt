package com.tools.payhelper.pay.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.jingyu.pay.ui.dashboard.SellViewModel
import com.jingyu.pay.ui.dashboard.SellViewModelFactory
import com.tools.payhelper.R
import com.tools.payhelper.pay.ToastManager

class ConfirmOrderActivity : AppCompatActivity() {

    val sellViewModel: SellViewModel by lazy {
        ViewModelProvider(this, SellViewModelFactory()).get(SellViewModel::class.java)
    }


    lateinit var closeBtn :Button
    lateinit var payName :TextView
    lateinit var getname :TextView
    lateinit var bank :TextView
    lateinit var price :TextView
    lateinit var ordernotext :TextView
    lateinit var okBtn :Button
    var  mSellListData: SellListData.Data? = null
    lateinit var nameEdt :EditText
    lateinit var priceEdt  :EditText


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        setContentView(R.layout.activity_confirm_order)

        ordernotext = findViewById(R.id.ordernotext);
        payName = findViewById(R.id.payname)
        getname = findViewById(R.id.getname)
        bank = findViewById(R.id.bank)
        price = findViewById(R.id.price)
        nameEdt = findViewById(R.id.nameEdt)
        priceEdt = findViewById(R.id.priceEdt)
        var _name :String = ""
        var _price :String = ""


        val json :String = intent.getStringExtra("json")!!
        if (!json!!.isEmpty()){
            mSellListData= Gson().fromJson(json,SellListData.Data::class.java)
            ordernotext.text = mSellListData!!.orderNo
            getname.text = "收款人姓名:" + mSellListData!!.userName
            payName.text = "打款人姓名:" + mSellListData!!.payUserName
            bank.text = "收款银行:" + mSellListData!!.bankName
            price.text = "￥"+mSellListData!!.score
        }

        closeBtn = findViewById(R.id.closeBtn)
        closeBtn.setOnClickListener {
            this.finish()
        }
        okBtn = findViewById(R.id.okBtn)
        okBtn.setOnClickListener {
            val payname: String = mSellListData!!.payUserName.substring(mSellListData!!.payUserName.length - 1)
            var pirceName : String = mSellListData!!.score.toString()
            if (!nameEdt.getText().toString().isEmpty()) {
                if (!nameEdt.getText().toString().equals(payname)) {
                    Toast.makeText(this, "名称输入错误", Toast.LENGTH_SHORT).show()

                    return@setOnClickListener
                } else {
                    _name = mSellListData!!.payUserName
                }
            } else {
                Toast.makeText(this, "名称输入错误", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (priceEdt.getText().toString().isEmpty()) {
                Toast.makeText(this, "金额输入错误", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                if (priceEdt.getText().toString() != pirceName) {
                    Toast.makeText(this, "金额输入错误", Toast.LENGTH_SHORT).show()

                    return@setOnClickListener
                } else {
                    pirceName = mSellListData!!.score.toString()
                }
            }

            sellViewModel.getComfirmOrder(mSellListData!!.id, _name, this).observe(this, Observer {
                if (it!=null){
                    runOnUiThread {
                        if (it.code==1){
                            ToastManager.showToastCenter(this, it.msg)
                            this.finish()

                        }

                    }
                }else{
                    ToastManager.showToastCenter(this, "令牌失效 请重新登入")

                }
            })


        }
    }
}