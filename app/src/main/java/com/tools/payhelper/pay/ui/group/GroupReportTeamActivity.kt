package com.jingyu.pay.ui.group

import android.annotation.SuppressLint
import android.content.Context
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
import com.jingyu.pay.ui.home.HomeFragment
import com.tools.payhelper.R
import com.tools.payhelper.pay.ui.group.ReportsTeamData
import org.w3c.dom.Text

class GroupReportTeamActivity : AppCompatActivity() {
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
    lateinit var name : TextView
    var adapter: Adapter? = null
    var buyDataList: ArrayList<ReportsTeamData.Data> = ArrayList()

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
        name = findViewById(R.id.name)

//        getReport("","0")
        getData("0")
        closebtn.setOnClickListener {
            finish()
        }
        group.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.rb_yestday ->
                    getData("-1")

                R.id.rb_today ->
                    getData("0")


            }


        }
        val recyclerView: RecyclerView =  findViewById(R.id.recycler_view)
        adapter = Adapter()

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter!!.updateList(buyDataList)

        recyclerView.adapter = adapter

        adapter!!.notifyDataSetChanged()


    }
    fun  getData(day : String){

        var accountId =   intent.getStringExtra("accountId")
        var loginId =   intent.getStringExtra("loginId")
        name.text = loginId

        getReport(accountId!!,day)

    }

    fun getReport( id : String, day : String){
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
    fun getReportTeam(id : String, day : String){
        groupViewModel.getReportTime(this,id,day).observe(this, Observer {
            if (it!=null){
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

            }
        })
    }

    class Adapter() : RecyclerView.Adapter<Adapter.ViewHolder>() {
        var bankCardInfoList:ArrayList<ReportsTeamData.Data>? = null


        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var bankName: TextView
            var cardNo: TextView
            var time: TextView
            var amount: TextView
            var orderno: TextView
            var addButton : Button
            var paymentxe : TextView


            init {
                bankName = view.findViewById(R.id.bankname)
                cardNo = view.findViewById(R.id.cardno)
                time = view.findViewById(R.id.time)
                amount = view.findViewById(R.id.amount)
                orderno = view.findViewById(R.id.orderno)
                addButton = view.findViewById(R.id.addbtn);
                paymentxe = view.findViewById(R.id.paymentxe)


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
            holder.orderno.text = "佣金:"+info.commission.toString()
            holder.cardNo.text = "小额买币:"+info.paymentXe.toString()
            holder.paymentxe.text = "小额买币笔数:"+info.paymentXeQty.toString()


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
