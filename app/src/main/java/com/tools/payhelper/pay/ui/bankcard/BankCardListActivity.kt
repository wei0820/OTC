package com.jingyu.pay.ui.bankcard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson

import com.tools.payhelper.AddWechatActivity
import com.tools.payhelper.Main22Activity
import com.tools.payhelper.R
import com.tools.payhelper.UploadAliPayPhotoActivity
import com.tools.payhelper.UploadPhotoActivity
import com.tools.payhelper.pay.PayHelperUtils
import com.tools.payhelper.pay.ToastManager
import com.tools.payhelper.pay.ui.bankcard.AddBankDialog
import com.tools.payhelper.AddBankQrcodeActivity
import com.tools.payhelper.UpadteAddWechatActivity
import com.tools.payhelper.UpdateAddBankQrcodeActivity
import com.tools.payhelper.UpdateMain22Activity
import com.tools.payhelper.UpdateUploadAliPayPhotoActivity
import com.tools.payhelper.pay.ui.bankcard.AddCardDialog
import com.tools.payhelper.pay.ui.bankcard.AddPayCardDialog
import com.tools.payhelper.pay.ui.bankcard.AddToCardDialog
import com.tools.payhelper.pay.ui.bankcard.AddWechatPhoneDialog
import com.tools.payhelper.pay.ui.bankcard.BanCardListData
import com.tools.payhelper.pay.ui.bankcard.UpdateAddBankDialog
import com.tools.payhelper.pay.ui.bankcard.UpdateAddCardDialog
import com.tools.payhelper.pay.ui.bankcard.UpdateAddPayCardDialog
import com.tools.payhelper.pay.ui.bankcard.UpdateAddWechatPhoneDialog

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
            getString(R.string.add_pay),  getString(R.string.add_scan),getString(R.string.add_upload3),getString(R.string.add_upload2),getString(R.string.add_wechat),getString(R.string.add_upload),getString(R.string.add_wechat_phone)
        ,getString(R.string.add_bank))
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
                    getString(R.string.add_scan) -> {
                        val intent  = Intent()
                        intent.setClass(this, Main22Activity::class.java)
                        startActivity(intent)
                    }
                    getString(R.string.add_wechat) -> {
                        val intent  = Intent()
                        intent.setClass(this, AddWechatActivity::class.java)
                        startActivity(intent)
                    }

                    getString(R.string.add_wechat_phone) -> {
                        val dialog = AddWechatPhoneDialog(this)
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

                    getString(R.string.add_bank) -> {
                        val dialog = AddBankDialog(this)
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

                    getString(R.string.add_upload) -> {
                        val intent  = Intent()
                        intent.setClass(this, UploadPhotoActivity::class.java)
                        startActivity(intent)
                    }
                    getString(R.string.add_upload2) -> {
                        val intent  = Intent()
                        intent.setClass(this, UploadAliPayPhotoActivity::class.java)
                        startActivity(intent)
                    }

                    getString(R.string.add_upload3) -> {
//                        val intent  = Intent()
//                        intent.setClass(this, AddBankQrcodeActivity::class.java)
//                        startActivity(intent)

                        val dialog = AddToCardDialog(this)
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

    override fun onResume() {
        super.onResume()
        getBankCardList()
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
                    PayHelperUtils.setSaveALLBankCardData(this,buyDataList);

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
            var lockName : TextView
            var ischeck : TextView
            var layout : LinearLayout
            var collectionlimittext : TextView



            init {
                accountNo = view.findViewById(R.id.accountNo)
                accountInfo = view.findViewById(R.id.accountInfo)
                switchButton = view.findViewById(R.id.switchButton)
                remove_btn = view.findViewById(R.id.remove_btn)
                subName = view.findViewById(R.id.subname)
                userName = view.findViewById(R.id.username)
                pinName = view.findViewById(R.id.pin)
                lockName= view.findViewById(R.id.lock)
                ischeck  = view.findViewById(R.id.ischeck);
                layout = view.findViewById(R.id.layout)
                collectionlimittext = view.findViewById(R.id.collectionlimittext)

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
            if (info.lock!=null){
                if (!info.lock.isEmpty()){
                    holder.lockName.text =  "备注" +info.lock
                }else {
                    holder.lockName.text = "备注" + ""

                }

            }else{
                holder.lockName.text = "备注" + ""
            }


            holder.switchButton.isChecked = info.isEnable


            holder.remove_btn.setOnClickListener {
                mActivity.deleteBankCard(info.id)
            }


            holder.switchButton.setOnClickListener {

               mActivity.setStopBankCard(info.id)

            }

            if (info.isAWXe==true){
                holder.ischeck.text = "是否只用于小额收款:"+"是"

            }else{
                holder.ischeck.text = "是否只用于小额收款:"+"否"

            }
            holder.collectionlimittext.text = "收款:" +info.collectionlimit

            holder.layout.setOnLongClickListener {
                when(info.bankName){

                    mActivity.getString(R.string.add_pay)->{
                        val dialog = UpdateAddPayCardDialog(mActivity,info)
                        dialog.setAddBankCallback {
                            if (it!=null){
                                mActivity.runOnUiThread {
                                    ToastManager.showToastCenter(mActivity,it.msg)
                                    mActivity.getBankCardList()

                                }
                            }
                        }
                        dialog.show()


                    }
                    mActivity.getString(R.string.add_scan)->{
                        if (info!=null){
                            var intent = Intent()
                            intent.setClass(mActivity, UpdateMain22Activity::class.java)
                            intent.putExtra("json", Gson().toJson(info))
                            mActivity.startActivity(intent)
                        }else{
                            Toast.makeText(mActivity,"发生错误 请重新登入", Toast.LENGTH_SHORT).show()

                        }

                    }
                    mActivity.getString(R.string.add_wechat)->{
                        if (info!=null){
                            var intent = Intent()
                            intent.setClass(mActivity, UpadteAddWechatActivity::class.java)
                            intent.putExtra("json", Gson().toJson(info))
                            mActivity.startActivity(intent)
                        }else{
                            Toast.makeText(mActivity,"发生错误 请重新登入", Toast.LENGTH_SHORT).show()

                        }
                    }
                    mActivity.getString(R.string.add_wechat_phone)->{
                        val dialog = UpdateAddWechatPhoneDialog( mActivity,info)
                        dialog.setAddBankCallback {
                            if (it!=null){
                                mActivity.runOnUiThread {
                                    ToastManager.showToastCenter( mActivity,it.msg)
                                    mActivity.getBankCardList()

                                }
                            }
                        }
                        dialog.show()

                    }
                    mActivity.getString(R.string.add_upload)->{
                        if (info!=null){
                            var intent = Intent()
                            intent.setClass(mActivity, UpadteAddWechatActivity::class.java)
                            intent.putExtra("json", Gson().toJson(info))
                            mActivity.startActivity(intent)
                        }else{
                            Toast.makeText(mActivity,"发生错误 请重新登入", Toast.LENGTH_SHORT).show()

                        }
                    }
                    mActivity.getString(R.string.add_upload2)->{
                        if (info!=null){
                            var intent = Intent()
                            intent.setClass(mActivity, UpdateUploadAliPayPhotoActivity::class.java)
                            intent.putExtra("json", Gson().toJson(info))
                            mActivity.startActivity(intent)
                        }else{
                            Toast.makeText(mActivity,"发生错误 请重新登入", Toast.LENGTH_SHORT).show()

                        }
                    }
                    mActivity.getString(R.string.add_upload3)->{
                        if (info!=null){
                            var intent = Intent()
                            intent.setClass(mActivity, UpdateAddBankQrcodeActivity::class.java)
                            intent.putExtra("json", Gson().toJson(info))
                            mActivity.startActivity(intent)
                        }else{
                            Toast.makeText(mActivity,"发生错误 请重新登入", Toast.LENGTH_SHORT).show()

                        }
                    }

                    mActivity.getString(R.string.add_bank)->{


                        val dialog = UpdateAddBankDialog(mActivity,info)
                        dialog.setAddBankCallback {
                            if (it!=null){
                                mActivity.runOnUiThread {
                                    ToastManager.showToastCenter(mActivity,it.msg)
                                    mActivity.getBankCardList()

                                }
                            }
                        }
                        dialog.show()

                    }
                    else -> {

                        val dialog = UpdateAddCardDialog(mActivity,info)
                        dialog.setAddBankCallback {
                            if (it!=null){
                                mActivity.runOnUiThread {
                                    ToastManager.showToastCenter(mActivity,it.msg)
                                    mActivity.getBankCardList()

                                }
                            }
                        }
                        dialog.show()
                    }

                }
                true

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