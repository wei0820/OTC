package com.jingyu.pay.ui.bankcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.jingyu.pay.ui.group.*
import com.tools.payhelper.R
import com.tools.payhelper.pay.ToastManager
import com.tools.payhelper.pay.ui.bankcard.AddCardDialog
import com.tools.payhelper.pay.ui.bankcard.AddPayCardDialog
import com.tools.payhelper.pay.ui.bankcard.BanCardListData

class BankCardListActivity : AppCompatActivity() {
    lateinit var  close :ImageButton
    lateinit var fab: FloatingActionButton
    lateinit  var recyclerView: RecyclerView
    var adapter: Adapter? = null
    var buyDataList: ArrayList<BanCardListData.Data> = ArrayList()
    val bankCardViewModel: BankCardViewModel by lazy {
        ViewModelProvider(this, BankCradViewModelFactory()).get(BankCardViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_card_list)
        title = "银行卡"
        fab  = findViewById(R.id.normalFAB)
        recyclerView =  findViewById(R.id.recyclerView)
        close = findViewById(R.id.closeBtn)
        close.setOnClickListener {
            finish()
        }
        getBankCardList()
        adapter = Adapter(this)

        recyclerView!!.layoutManager = LinearLayoutManager(this)
        adapter!!.updateList(buyDataList)

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

        fab.setOnClickListener {
            addAlert()


        }

    }
    private lateinit var lunch: List<String>

    fun addAlert(){
        lunch = listOf(getString(R.string.add_bankcard),
            getString(R.string.add_pay), )
        AlertDialog.Builder(this@BankCardListActivity)
            .setItems(lunch.toTypedArray()) { _, which ->
                val name = lunch[which]
                when(name){
                    getString(R.string.add_bankcard) -> {
                        val dialog = AddCardDialog(this)
                        dialog.setAddBankCallback {
                            if (it!=null){
                                runOnUiThread {
                                    ToastManager.showToastCenter(this,it.msg)
                                    getBankCardList()

                                }
                            }
                        }
                        dialog.show()
                    }
                    getString(R.string.add_pay) -> {

                        val dialog = AddPayCardDialog(this)
                        dialog.setAddBankCallback {
                            if (it!=null){
                                runOnUiThread {
                                    ToastManager.showToastCenter(this,it.msg)
                                    getBankCardList()

                                }
                            }
                        }
                        dialog.show()
                    }

                }
            }
            .show()
    }

    fun getBankCardList(){
        bankCardViewModel.getBankList(this).observe(this, Observer {
            buyDataList.clear()
            if (it.code == 0){
                if (it.data!=null){
                    for (datum in it.data) {
                        buyDataList.add(datum)
                        recyclerView.post(Runnable { adapter!!.notifyDataSetChanged() })

                    }

                }
            }
        })
    }
    fun deleteBankCard(id:String){
        bankCardViewModel.deleteBankCard(this,id).observe(this, Observer {
            if (it!=null){
                runOnUiThread {
                    ToastManager.showToastCenter(this,it.msg)
                    getBankCardList()

                }

            }
        })

    }

    fun  setStopBankCard(id: String){
        bankCardViewModel.setStopBankCard(this,id).observe(this, Observer {
            if (it!=null){
                runOnUiThread {
                    ToastManager.showToastCenter(this,it.msg)
                    getBankCardList()

                }
            }
        })


    }



    class Adapter(activity: BankCardListActivity) : RecyclerView.Adapter<Adapter.ViewHolder>() {
        var bankCardInfoList:ArrayList<BanCardListData.Data>? = null
        var mActivity = activity

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
                var accountNo : TextView
                var accountInfo : TextView
                var switchButton : Switch
                var remove_btn : ImageView
                var subName : TextView
                var userName : TextView
                var pinName : TextView


            init {
                accountNo = view.findViewById(R.id.accountNo)
                accountInfo = view.findViewById(R.id.accountInfo)
                switchButton = view.findViewById(R.id.switchButton)
                remove_btn = view.findViewById(R.id.remove_btn)
                subName = view.findViewById(R.id.subname)
                userName = view.findViewById(R.id.username)
                pinName = view.findViewById(R.id.pin)

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.bankcard_item_view, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val info: BanCardListData.Data = bankCardInfoList!!.get(position)
            holder.accountNo.text = info.cardNo
            holder.accountInfo.text = info.bankName
            holder.subName.text = info.subName
            holder.userName.text = info.userName
            holder.pinName.text = info.pinYin

            holder.switchButton.isChecked = info.isEnable


            holder.remove_btn.setOnClickListener {
                mActivity.deleteBankCard(info.id)
            }

//            holder.switchButton.setOnCheckedChangeListener { compoundButton, b ->
//                if(info.isEnable)
//
//
//
//            }

            holder.switchButton.setOnClickListener {
                                mActivity.setStopBankCard(info.id)

            }

        }

        override fun getItemCount(): Int {
            return bankCardInfoList!!.size
        }

        //更新資料用
        fun updateList(list:ArrayList<BanCardListData.Data>){
            bankCardInfoList = list
        }
    }

}