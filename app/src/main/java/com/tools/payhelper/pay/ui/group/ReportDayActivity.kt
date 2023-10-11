package com.jingyu.pay.ui.group

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jingyu.pay.ui.sellrecord.Adapter
import com.tools.payhelper.R
import com.tools.payhelper.pay.ui.group.ReportDayData
import java.util.ArrayList

class ReportDayActivity : AppCompatActivity() {
    var adapter: Adapter? = null
    var reportDay: ArrayList<ReportDayData.Data> = ArrayList()
    val groupViewModel: GroupViewModel by lazy {
        ViewModelProvider(this, GroupViewModelFactory()).get(GroupViewModel::class.java)
    }

    lateinit var recyclerView: RecyclerView
    lateinit var closebtn : ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_day)
        title = "结算日报"
        getListItem()
        adapter = Adapter()
        recyclerView = findViewById(R.id.recycler_view)
        closebtn = findViewById(R.id.closeBtn)
        closebtn.setOnClickListener {
            finish()
        }
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        adapter!!.updateList(reportDay)

        recyclerView!!.adapter = adapter

        adapter!!.notifyDataSetChanged()

    }

    fun getListItem(){
        groupViewModel.getGroupReportDay(this).observe(this, Observer {
            reportDay.clear()
            if (it.code == 0){
                if (it.data!=null){
                    for (datum in it.data) {
                        reportDay.add(datum)

                        adapter!!.notifyDataSetChanged()
                    }
                    if (reportDay.size<=0){
                        adapter!!.notifyDataSetChanged()

                    }
                }
            }
        })
    }

    class Adapter() : RecyclerView.Adapter<Adapter.ViewHolder>() {
        var bankCardInfoList: ArrayList<ReportDayData.Data>? = null


        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var bankName: TextView
            var cardNo: TextView
            var time: TextView
            var amount: TextView
            var orderno: TextView
            var frozen : TextView


            init {
                bankName = view.findViewById(R.id.bankname)
                cardNo = view.findViewById(R.id.cardno)
                time = view.findViewById(R.id.time)
                amount = view.findViewById(R.id.amount)
                orderno = view.findViewById(R.id.orderno)
                frozen = view.findViewById(R.id.frozen);


            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.reportday_list_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val info: ReportDayData.Data = bankCardInfoList!!.get(position)

            holder.orderno.text = info.id
            holder.cardNo.text = info.score.toString()
            holder.time.text = info.created



        }

        override fun getItemCount(): Int {
            return bankCardInfoList!!.size
        }

        //更新資料用
        fun updateList(list: ArrayList<ReportDayData.Data>){
            bankCardInfoList = list
        }


    }
}