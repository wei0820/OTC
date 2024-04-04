package com.tools.payhelper.pay.ui.payment

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jingyu.pay.ui.bankcard.*
import com.tools.payhelper.R
import com.tools.payhelper.pay.AddBuySettingDilog
import com.tools.payhelper.pay.PayHelperUtils
import com.tools.payhelper.pay.ToastManager
import com.tools.payhelper.pay.ui.bankcard.AddPayCardDialog
import com.tools.payhelper.pay.ui.bankcard.BanCardListData

class PaymentActivity : AppCompatActivity() {
    val paymentViewModel: PaymentViewModel by lazy {
        ViewModelProvider(this, PaymentViewModelFactory()).get(PaymentViewModel::class.java)
    }
    var buyDataList: ArrayList<BanCardListData.Data> = ArrayList()
    lateinit var  close : ImageButton
    lateinit var fab: FloatingActionButton
    lateinit var recyclerView: RecyclerView
    var adapter: Adapter? = null
    var PaymentList: ArrayList<PaymentData.Data> = ArrayList()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        close = findViewById(R.id.closeBtn)
        recyclerView = findViewById(R.id.recyclerView)

        close.setOnClickListener {
            finish()
        }
//        button = findViewById(R.id.button)
//        button.setOnClickListener {
//            PayHelperUtils.getALLBankCardData(this).forEach {
//                if (it!=null){
//                    Log.d("Jack","PayHelperUtils")
//
//                    Log.d("Jack",it.cardNo)
//                }else{
//                    Log.d("","no")
//                }
//            }
//        }

        fab = findViewById(R.id.normalFAB)

        fab.setOnClickListener {
            val dialog = AddPaymentDialog(this)
//            dialog.setAddBankCallback {
//                if (it!=null){
//                    runOnUiThread {
//                        ToastManager.showToastCenter(this,it.msg)
//                        getBankCardList()
//
//                    }
//                }
//            }
            dialog.show()
        }

        adapter = Adapter(this)

        recyclerView!!.layoutManager = LinearLayoutManager(this)
        adapter!!.updateList(PaymentList)

        recyclerView!!.adapter = adapter

        adapter!!.notifyDataSetChanged()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // if the recycler view is scrolled
                // above hide the FAB
                if (dy > 10 && fab.isShown) {
                    fab.hide()
                }

                // if the recycler view is
                // scrolled above show the FAB
                if (dy < -10 && !fab.isShown) {
                    fab.show()
                }

                // of the recycler view is at the first
                // item always show the FAB
                if (!recyclerView.canScrollVertically(-1)) {
                    fab.show()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == 0){
                    fab.show()
                }

            }
        })
    }

    override fun onResume() {
        super.onResume()
        getBankList()
        getPaymentList()
    }

    fun getPaymentList(){
        paymentViewModel.getPaymentList(this).observe(this, Observer {
            PaymentList.clear()
            if (it.code == 0){
                if (it.data!=null){
                    for (datum in it.data) {
                        PaymentList.add(datum)
                        recyclerView.post(Runnable { adapter!!.notifyDataSetChanged() })

                    }
                }
            }
        })
    }

    fun getBankList(){
        paymentViewModel.getBankList(this).observe(this, Observer {
            if (it.code == 0){
                if (it.data!=null){
                    for (datum in it.data) {
                        buyDataList.add(datum)
                    }
                    PayHelperUtils.setSaveALLBankCardData(this,buyDataList);

                }
            }
        })
    }
    class Adapter(activity: PaymentActivity) : RecyclerView.Adapter<Adapter.ViewHolder>() {
        var paymentInfoList:ArrayList<PaymentData.Data>? = null
        var mActivity = activity

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var TextView1 : TextView
            var TextView2 : TextView
            var TextView3 : TextView
            var TextView4 : TextView
            var TextView5 : TextView
            var TextView6 : TextView
            var TextView7 : TextView



            init {
                TextView1 = view.findViewById(R.id.text1)
                TextView2 = view.findViewById(R.id.text2)
                TextView3 = view.findViewById(R.id.text3)
                TextView4 = view.findViewById(R.id.text4)
                TextView5 = view.findViewById(R.id.text5)
                TextView6 = view.findViewById(R.id.text6)
                TextView7 = view.findViewById(R.id.text7)



            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.payment_item_view, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val info: PaymentData.Data = paymentInfoList!!.get(position)
            var stateString = ""
            when (info.state){
                0 -> stateString = "申请中"
                1 ->stateString =  "支付中"
                2 -> stateString = "已完成"
                else ->stateString = "已取消"


            }
            holder.TextView1.text = "状态:" + stateString
            holder.TextView2.text = "金额:" +info.score
            holder.TextView3.text = "申请时间:" +info.created
            holder.TextView4.text = "银行:" +info.bankName
            holder.TextView5.text = "姓名:" +info.userName
            holder.TextView6.text = "卡号:" + info.cardId
            holder.TextView7.text = "說明:" +info.remark






        }

        override fun getItemCount(): Int {
            return paymentInfoList!!.size
        }

        //更新資料用
        fun updateList(list:ArrayList<PaymentData.Data>){
            paymentInfoList = list
        }
    }

}