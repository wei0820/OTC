package com.jingyu.pay.ui.group

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tools.payhelper.R
import com.tools.payhelper.pay.PayHelperUtils
import com.tools.payhelper.pay.ui.group.AddAllDataDialog
import com.tools.payhelper.pay.ui.group.ReportsTeamData

class GroupReportActivity : AppCompatActivity() {
    lateinit var closebtn : ImageButton
    lateinit var group : RadioGroup
    lateinit var rb_yestoday : RadioButton
    lateinit var rb_today : RadioButton
    val groupViewModel: GroupViewModel by lazy {
        ViewModelProvider(this, GroupViewModelFactory()).get(GroupViewModel::class.java)
    }
    lateinit var  Text1 : TextView
    lateinit var  Text2 : TextView
    lateinit var  Text3 : TextView
    lateinit var  Text4 : TextView
    lateinit var  Text5 : TextView
    lateinit var  Text6 : TextView
    lateinit var  mName : TextView
    lateinit var allbutton :Button

    var adapter: Adapter? = null
    var buyDataList: ArrayList<ReportsTeamData.Data> = ArrayList()
    var paymentnmutableList : ArrayList<Double> = ArrayList()
    var collectionmutableList : ArrayList<Double> = ArrayList()

    var paymentXeCommutableList : ArrayList<Double> = ArrayList()
    var commissionmutableList : ArrayList<Double> = ArrayList()
    var paymentXemutableList : ArrayList<Double> = ArrayList()
    var paymentXeQtymutableList : ArrayList<Double> = ArrayList()

    var titleStr = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_report)
        closebtn = findViewById(R.id.closeBtn)
        group = findViewById(R.id.group_radio)
        rb_yestoday = findViewById(R.id.rb_yestday)
        rb_today = findViewById(R.id.rb_today)
        group.check(R.id.rb_today)
        Text1 = findViewById(R.id.text1)
        Text2 = findViewById(R.id.text2)
        Text3 = findViewById(R.id.text3)
        Text4 = findViewById(R.id.text4)
        Text5 = findViewById(R.id.text5)
        Text6 = findViewById(R.id.text6)
        mName = findViewById(R.id.name)
        allbutton = findViewById(R.id.allbutton);

        mName.text = PayHelperUtils.getUserName(this)
        getReport("","0")
        closebtn.setOnClickListener {
            finish()
        }
        group.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.rb_yestday ->

                    getReport("","-1")

                R.id.rb_today ->
                    getReport("","0")


            }


        }
        val recyclerView: RecyclerView =  findViewById(R.id.recycler_view)
        adapter = Adapter(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter!!.updateList(buyDataList)

        recyclerView.adapter = adapter

        adapter!!.notifyDataSetChanged()

        allbutton.setOnClickListener {

            var titletxtString = titleStr

            var paymentString = paymentnmutableList.sum().toString()
            var collectionString = collectionmutableList.sum().toString()
            var commissionString = commissionmutableList.sum().toString()
            var paymentXeString = paymentXemutableList.sum().toString()
            var paymentXeQtyString = paymentXeQtymutableList.sum().toString()
//            var paymentXeComString = paymentXeCommutableList.sum().toString()





            val dialog = AddAllDataDialog(this,
                titletxtString,paymentString,collectionString,commissionString, paymentXeString,
                paymentXeQtyString)
            dialog.setAddBankCallback {
                if (it){
                }
            }
            dialog.show()


        }
    }
    fun clearAllData(){
        collectionmutableList.clear()
        commissionmutableList.clear()
        paymentXemutableList.clear()
        paymentXeQtymutableList.clear()
        paymentnmutableList.clear()
//        paymentXeCommutableList.clear()
    }

    fun addTeamAllData(data :ReportsTeamData.Data){
        collectionmutableList.add(data.collection)
        commissionmutableList.add(data.commission)
        paymentXemutableList.add(data.paymentXe)
        paymentXeQtymutableList.add(data.paymentXeQty)
        paymentnmutableList.add(data.payment)
//        paymentXeCommutableList.add(data.paymentXeRebate)



    }

    fun getReport( id : String, day : String){
        if (day== "0"){
            titleStr = "今日汇总报表"
        }else{
            titleStr = "昨日汇总报表"
        }

        groupViewModel.getReport(this,id,day).observe(this, Observer {
            if (it!=null){
                Text1.text = it.data.payment.toString()
                Text2.text = it.data.collection.toString()
                Text3.text = it.data.commission.toString()
                Text4.text = it.data.paymentXe.toString()
                Text5.text = it.data.paymentXeQty.toString()
                Text6.text = it.data.paymentXeCom.toString()



            }
        })

        getReportTeam(id,day)

    }
    fun toActivity(info :ReportsTeamData.Data){
        var intent = Intent();
        var bundle = Bundle()
        bundle.putString("accountId",info.accountId)
        bundle.putString("loginId",info.loginId)
        intent.putExtras(bundle)
        intent.setClass(this,GroupReportTeamActivity::class.java)
        startActivity(intent)
    }
    fun getReportTeam(id : String, day : String){



        groupViewModel.getReportTime(this,id,day).observe(this, Observer {
            if (it!=null){
                buyDataList.clear()
                clearAllData()
                if (it.code == 0){
                    if (it.data!=null){
                        for (datum in it.data) {
                            buyDataList.add(datum)
                            addTeamAllData(datum)

                            adapter!!.notifyDataSetChanged()
                        }
                        if (buyDataList.size<=0){
                            adapter!!.notifyDataSetChanged()

                        }
                    }
                }

            }
        })
    }

    class Adapter(activity: Activity) : RecyclerView.Adapter<Adapter.ViewHolder>() {
        var bankCardInfoList:ArrayList<ReportsTeamData.Data>? = null
        var mActivity  = activity

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var bankName: TextView
            var cardNo: TextView
            var time: TextView
            var amount: TextView
            var orderno: TextView
            var addButton : Button
            var paymentxe : TextView
            var  paymentxex : TextView


            init {
                bankName = view.findViewById(R.id.bankname)
                cardNo = view.findViewById(R.id.cardno)
                time = view.findViewById(R.id.time)
                amount = view.findViewById(R.id.amount)
                orderno = view.findViewById(R.id.orderno)
                addButton = view.findViewById(R.id.addbtn);
                paymentxe = view.findViewById(R.id.paymentxe)
                paymentxex = view.findViewById(R.id.paymentxex)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.group_report_list_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val info: ReportsTeamData.Data = bankCardInfoList!!.get(position)

            holder.amount.text = info.loginId

            holder.orderno.text = "买币:"+info.payment.toString()
            holder.bankName.text = "卖币:"+info.collection.toString()
            holder.cardNo.text = "佣金:"+info.commission.toString()
            holder.paymentxe.text = "小额买币:"+info.paymentXe.toString()
            holder.paymentxex.text = "小额买币笔数:"+info.paymentXeQty.toString()

            holder.addButton.setOnClickListener {

                if (info!=null){
                    var intent = Intent();
                    var bundle = Bundle()
                    bundle.putString("accountId",info.accountId)
                    bundle.putString("loginId",info.loginId)
                    intent.putExtras(bundle)
                    intent.setClass(mActivity,GroupReportTeamActivity::class.java)
                    mActivity.startActivity(intent)
                }

            }


        }

        override fun getItemCount(): Int {
            return bankCardInfoList!!.size
        }

        //更新資料用
        fun updateList(list:ArrayList<ReportsTeamData.Data>){
            bankCardInfoList = list
        }


    }

}
