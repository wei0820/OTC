package com.tools.payhelper.pay.ui.money

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jingyu.pay.ui.bankcard.*
import com.jingyu.pay.ui.notifications.TransferMoneyViewModel
import com.jingyu.pay.ui.notifications.TransferMoneyViewModelFactory
import com.tools.payhelper.R


class TransListActivity : AppCompatActivity() {
    val transferMoneyViewModel: TransferMoneyViewModel by lazy {
        ViewModelProvider(this, TransferMoneyViewModelFactory()).get(TransferMoneyViewModel::class.java)
    }
    lateinit var  close : ImageButton
    lateinit var fab: FloatingActionButton
    lateinit var recyclerView: RecyclerView
    var adapter: Adapter? = null


    var mTransferListData: ArrayList<TransferListData.Data> = ArrayList()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        close = findViewById(R.id.closeBtn)
        recyclerView = findViewById(R.id.recyclerView)

        close.setOnClickListener {
            finish()
        }



        fab = findViewById(R.id.normalFAB)

        fab.setOnClickListener {
            startActivity(Intent().setClass(this, TransferMoneyActivity::class.java))


        }

        adapter = Adapter(this)

        recyclerView!!.layoutManager = LinearLayoutManager(this)
        adapter!!.updateList(mTransferListData)

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
        getList()

    }
    fun getList(){
        transferMoneyViewModel.getTransList(this).observe(this, Observer {
            mTransferListData.clear()
            if (it.code == 0){
                if (it.data!=null){
                    for (datum in it.data) {
                        mTransferListData.add(datum)
                        recyclerView.post(Runnable { adapter!!.notifyDataSetChanged() })

                    }
                }
            }
        })
    }




    class Adapter(activity: TransListActivity) : RecyclerView.Adapter<Adapter.ViewHolder>() {
        var mTransferListData:ArrayList<TransferListData.Data>? = null
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
            val info: TransferListData.Data = mTransferListData!!.get(position)
            var stateString = ""
            when (info.inOut){
                true -> stateString = "转出"
                false ->stateString =  "转入"
                else ->stateString = "已取消"


            }
            holder.TextView1.text = "接收帐号:" + info.remark
            holder.TextView2.text = "金额:" +info.score
            holder.TextView3.text = "申请时间:" +info.created
            holder.TextView4.text = "交易方式:" +stateString
            holder.TextView5.text = ""
            holder.TextView6.text =  ""
            holder.TextView7.text =  ""
//
//




        }

        override fun getItemCount(): Int {
            return mTransferListData!!.size
        }
//
        fun updateList(list:ArrayList<TransferListData.Data>){
            mTransferListData = list
        }
    }

}