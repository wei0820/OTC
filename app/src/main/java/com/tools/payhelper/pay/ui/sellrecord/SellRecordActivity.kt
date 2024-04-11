package com.jingyu.pay.ui.sellrecord

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tools.payhelper.R
import com.tools.payhelper.pay.ui.sellrecord.SellRecordData
import java.text.DecimalFormat

import java.text.SimpleDateFormat
import java.util.*




class SellRecordActivity : AppCompatActivity() , DatePickerDialog.OnDateSetListener{
    lateinit var dateTextView: TextView
    lateinit var okButton: Button
    lateinit var recyclerView: RecyclerView
    lateinit var closebtn :ImageButton
    val merchantOrdersViewModel: SellRecodeViewModel by lazy {
        ViewModelProvider(this, SellRecordViewModelFactory()).get(SellRecodeViewModel::class.java)
    }

    var adapter: Adapter? = null

    var buyDataList: ArrayList<SellRecordData.Data> = ArrayList()
    lateinit var   spinner : Spinner;
    var dateString = ""
    var extraDuble :Double  = 7.5

    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_record)
        dateTextView = findViewById(R.id.dayedt)
        title = "卖币记录"
        recyclerView = findViewById(R.id.recycler_view)
        okButton = findViewById(R.id.datebtn)
        closebtn = findViewById(R.id.closeBtn)
        spinner = findViewById(R.id.spinner)

        closebtn.setOnClickListener {
            finish()
        }
        okButton.setOnClickListener {

            showDatePickerDialog()

        }

        dateTextView.text = getTodayTime()

        getList(getTodayTime().toString())

        adapter = Adapter(this)

        recyclerView!!.layoutManager = LinearLayoutManager(this)
        adapter!!.updateList(buyDataList)

        recyclerView!!.adapter = adapter

        adapter!!.notifyDataSetChanged()

        val adapter = ArrayAdapter.createFromResource(this,
            R.array.spinner_item,
            android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(0, false)
        spinner.onItemSelectedListener = spnOnItemSelected



    }

    fun get(){
        merchantOrdersViewModel.getExrateData(this).observe(this, androidx.lifecycle.Observer {
            if (it!=null){
                if (it.data!=null){
                    extraDuble = it.data.exrateDoubel
                }
            }
        })
    }




    fun getList(s:String){
        merchantOrdersViewModel.getSellRecodeList(this,s).observe(this, androidx.lifecycle.Observer {
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

    fun getSelectList(s:String,type: String){
        merchantOrdersViewModel.getSellRecodeList(this,s).observe(this, androidx.lifecycle.Observer {
            buyDataList.clear()
            if (it.code == 0){
                if (it.data!=null){
                    for (datum in it.data) {
                        if (type=="全部"){
                            buyDataList.add(datum)
                            adapter!!.notifyDataSetChanged()
                        }else{
                            when(type){
                                "进行中" ->
                                    if (datum.state==0||datum.state==1){
                                        buyDataList.add(datum)
                                        adapter!!.notifyDataSetChanged()
                                    }
                                "已完成" ->
                                    if (datum.state==2){
                                        buyDataList.add(datum)
                                        adapter!!.notifyDataSetChanged()
                                    }
                                "已超时" ->
                                    if (datum.state==3){
                                        buyDataList.add(datum)
                                        adapter!!.notifyDataSetChanged()
                                    }

                            }

                        }




                    }
                    if (buyDataList.size<=0){
                        adapter!!.notifyDataSetChanged()

                    }
                }
            }
        })
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
    override fun onDateSet(p0: DatePicker?, year: Int, dayOfMonth: Int, month: Int) {
        val date = "month/day/year: " + month.toString() + "/" + dayOfMonth.toString() + "/" + year
        Log.d("Jack",date)
        var string = year.toString()  +  "-" +(dayOfMonth+1).toString() + "-" + month.toString()
        dateTextView.text = string
        dateString = string
        getList(string)
        spinner.setSelection(0, false)



    }
    fun getTodayTime(): String? {
        //String dateformat = "yyyyMMdd"; 成果圖第一個日期顯示的格式
        val dateformat = "yyyy-MM-dd" //日期的格式(第二個)
        val mCal = Calendar.getInstance()
        val df = SimpleDateFormat(dateformat)
        return df.format(mCal.time)
    }


    private val spnOnItemSelected: AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?,
                pos: Int, id: Long,
            ) {
                val sPos = pos.toString()
                val sInfo = parent.getItemAtPosition(pos).toString()
                //String sInfo=parent.getSelectedItem().toString();
                Log.d("Jack","選項$sPos:$sInfo")
                getSelectList(dateString,sInfo)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }

}


class Adapter(activity: SellRecordActivity) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    var bankCardInfoList:ArrayList<SellRecordData.Data>? = null
    var mActivity = activity

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var bankName: TextView
        var cardNo: TextView
        var time: TextView
        var amount: TextView
        var orderN0:TextView
        var cBankName : TextView
        var cName : TextView
        var exrate: TextView
        var usdt: TextView

        init {
            orderN0 = view.findViewById(R.id.orderno);
            bankName = view.findViewById(R.id.bankname)
            cardNo = view.findViewById(R.id.cardno)
            time = view.findViewById(R.id.time)
            amount = view.findViewById(R.id.amount)
            cBankName = view.findViewById(R.id.cbankname);
            cName = view.findViewById(R.id.cname)
            exrate = view.findViewById(R.id.exrate)
            usdt = view.findViewById(R.id.usdt)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.sell_record_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info: SellRecordData.Data = bankCardInfoList!!.get(position)
        val df = DecimalFormat("###.00")

        holder.orderN0.text = info.orderNo
        holder.bankName.text = "姓名:" + info.userName
        holder.cardNo.text = "卡号:" +  info.cardNo
        holder.time.text = info.created
        holder.amount.text = "￥"+info.commission +"/" +"￥"+info.score
        holder.cBankName.text = info.cBankName
        holder.cName.text = info.cUserName
        holder.exrate.text = "单价:"+ mActivity.extraDuble
        holder.usdt.text = "成交金额USDT:"+ df.format(info.score/mActivity.extraDuble)



    }

    override fun getItemCount(): Int {
        return bankCardInfoList!!.size
    }

    //更新資料用
    fun updateList(list:ArrayList<SellRecordData.Data>){
        bankCardInfoList = list
    }




}