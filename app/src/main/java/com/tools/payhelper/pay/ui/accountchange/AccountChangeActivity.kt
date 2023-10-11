package com.jingyu.pay.ui.accountchange

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


import com.jingyu.pay.ui.notifications.PersonalViewModel
import com.jingyu.pay.ui.notifications.PersonalViewModelFactory
import com.jingyu.pay.ui.transaction.FrozenRecordsViewModel
import com.jingyu.pay.ui.transaction.FrozenRecordsViewModelFactory
import com.tools.payhelper.R
import com.tools.payhelper.databinding.ActivityAccountBinding
import com.tools.payhelper.pay.ui.accountchange.AccountChange
import java.text.SimpleDateFormat
import java.util.*

class AccountChangeActivity: AppCompatActivity()  {

    lateinit var  binding: ActivityAccountBinding;

    var adapter: Adapter? = null

    var buyDataList: ArrayList<AccountChange.Data> = ArrayList()


    val accountChangeViewModel: AccountChangeViewModel by lazy {
        ViewModelProvider(this, AccountChangeViewModelFactory()).get(AccountChangeViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "余额帐变"

        getList(getTodayTime().toString())

        binding.closeBtn.setOnClickListener {
            finish()
        }

        adapter = Adapter()

        binding.recyclerView!!.layoutManager = LinearLayoutManager(this)
        adapter!!.updateList(buyDataList)

        binding.recyclerView!!.adapter = adapter

        adapter!!.notifyDataSetChanged()



    }

    fun getList(s:String){
        accountChangeViewModel.getAccountChangeData(this).observe(this, Observer {
            buyDataList.clear()
            if (it.code == 0){
                if (it.data!=null){
                    for (datum in it.data) {
                        buyDataList.add(datum)

                        adapter!!.notifyDataSetChanged()
                    }
                    if (buyDataList.size<=0){
                        adapter!!.notifyDataSetChanged()

                    }
                }
            }
        })


    }


    fun getTodayTime(): String? {
        //String dateformat = "yyyyMMdd"; 成果圖第一個日期顯示的格式
        val dateformat = "yyyy-MM-dd" //日期的格式(第二個)
        val mCal = Calendar.getInstance()
        val df = SimpleDateFormat(dateformat)
        return df.format(mCal.time)
    }

}
class Adapter() : RecyclerView.Adapter<Adapter.ViewHolder>() {
    var bankCardInfoList:ArrayList<AccountChange.Data>? = null


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var bankName: TextView
        var cardNo: TextView
        var time: TextView
        var amount: TextView
        var orderN0:TextView
        var cBankName : TextView
        var cName : TextView


        init {
            orderN0 = view.findViewById(R.id.orderno);
            bankName = view.findViewById(R.id.bankname)
            cardNo = view.findViewById(R.id.cardno)
            time = view.findViewById(R.id.time)
            amount = view.findViewById(R.id.amount)
            cBankName = view.findViewById(R.id.cbankname);
            cName = view.findViewById(R.id.cname)



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.frozen_record_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info: AccountChange.Data = bankCardInfoList!!.get(position)
        holder.orderN0.text = info.id
        holder.bankName.text =  info.remark
        holder.cardNo.text = info.created
//        holder.time.text = info.created
        holder.amount.text = "￥"+info.score
        holder.cBankName.text = "￥"+info.quota.toString()
        holder.cName.text = "￥"+info.quotaEnd.toString()




    }

    override fun getItemCount(): Int {
        return bankCardInfoList!!.size
    }

    //更新資料用
    fun updateList(list:ArrayList<AccountChange.Data>){
        bankCardInfoList = list
    }


}