package com.jingyu.pay.ui.buyrecord

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tools.payhelper.R
import com.tools.payhelper.pay.ui.buyrecord.BuyRecordData

import java.text.SimpleDateFormat
import java.util.*

class BuyRecordActivity : AppCompatActivity() , DatePickerDialog.OnDateSetListener{
    lateinit var dateTextView: TextView
    lateinit var okButton: Button
    lateinit var recyclerView: RecyclerView
    lateinit var closebtn : ImageButton
    val merchantOrdersViewModel: BuyRecordViewModel by lazy {
        ViewModelProvider(this, BuyRecordViewModelFactory()).get(BuyRecordViewModel::class.java)
    }

    var adapter: Adapter? = null

    var buyDataList: ArrayList<BuyRecordData.Data> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_record)
        dateTextView = findViewById(R.id.dayedt)

        title = "买币记录"
        recyclerView = findViewById(R.id.recycler_view)
        okButton = findViewById(R.id.datebtn)

        closebtn = findViewById(R.id.closeBtn)
        closebtn.setOnClickListener {
            finish()
        }
        okButton.setOnClickListener {

            showDatePickerDialog()

        }
        dateTextView.text = getTodayTime()

        getList(getTodayTime().toString())

        adapter = Adapter()

        recyclerView!!.layoutManager = LinearLayoutManager(this)
        adapter!!.updateList(buyDataList)

        recyclerView!!.adapter = adapter

        adapter!!.notifyDataSetChanged()
    }

    fun getList(s:String){
        merchantOrdersViewModel.getBuyRecordList(this,s).observe(this, androidx.lifecycle.Observer {
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
            }        })
    }

    fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            this,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
    fun getTodayTime(): String? {
        //String dateformat = "yyyyMMdd"; 成果圖第一個日期顯示的格式
        val dateformat = "yyyy-MM-dd" //日期的格式(第二個)
        val mCal = Calendar.getInstance()
        val df = SimpleDateFormat(dateformat)
        return df.format(mCal.time)
    }
    override fun onDateSet(p0: DatePicker?, year: Int, dayOfMonth: Int, month: Int) {
        val date = "month/day/year: " + month.toString() + "/" + dayOfMonth.toString() + "/" + year
        Log.d("Jack",date)
        var string = year.toString()  +  "-" +(dayOfMonth+1).toString() + "-" + month.toString()
        dateTextView.text = string

        getList(string)



    }

}

class Adapter() : RecyclerView.Adapter<Adapter.ViewHolder>() {
    var bankCardInfoList:ArrayList<BuyRecordData.Data>? = null


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
            LayoutInflater.from(parent.context).inflate(R.layout.buy_record_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info: BuyRecordData.Data = bankCardInfoList!!.get(position)
        holder.orderN0.text = info.orderNo
        holder.bankName.text =info.bankName
        holder.cardNo.text = "卡号:" +  info.cardId
        holder.time.text = info.created
        holder.amount.text = "佣金￥"+info.commission + "/" +  "訂單金額"+info.score
        holder.cBankName.text = info.subName
        holder.cName.text = info.userName




    }

    override fun getItemCount(): Int {
        return bankCardInfoList!!.size
    }

    //更新資料用
    fun updateList(list:ArrayList<BuyRecordData.Data>){
        bankCardInfoList = list
    }


}
