package com.tools.payhelper.pay.ui.money

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.ClipboardManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jingyu.pay.ui.home.HomeFragment
import com.jingyu.pay.ui.notifications.TransferMoneyViewModel
import com.jingyu.pay.ui.notifications.TransferMoneyViewModelFactory
import com.squareup.picasso.Picasso
import com.tools.payhelper.R
import com.tools.payhelper.pay.Constant
import com.tools.payhelper.pay.PayHelperUtils
import com.tools.payhelper.pay.ToastManager
import com.tools.payhelper.pay.ui.order.PaymentMatchingData
import java.text.DecimalFormat

class BuyumoneyListActivity : AppCompatActivity() {
    lateinit var  mCopyTextView: TextView
    lateinit var okBtn : Button
    lateinit var closeBtn : ImageButton
    val transferMoneyViewModel: TransferMoneyViewModel by lazy {
        ViewModelProvider(this, TransferMoneyViewModelFactory()).get(TransferMoneyViewModel::class.java)
    }
    lateinit var  transEdit :EditText
    lateinit var scroeEditText: EditText
    lateinit var mGoogleEditText: EditText
    lateinit var qrimg : ImageView
    lateinit var mMonytttext : TextView
    lateinit var copybtn : Button
    lateinit var mHashvalueEditText: EditText
    var copytextt = ""
    var pprice = 0.0

    var mBuyUsdtListData: ArrayList<BuyUsdtListData.Data> = ArrayList()
    lateinit var recyclerView: RecyclerView
    var adapter_ing: IngAdapter? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.setBackgroundDrawableResource(android.R.color.transparent)
//        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        setContentView(R.layout.buy_money_list)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        checkUserinfo()
        recyclerView =  findViewById(R.id.recycler_view)
        adapter_ing = IngAdapter(this)
        closeBtn = findViewById(R.id.closeBtn)
        closeBtn.setOnClickListener {
            this.finish()
        }

        recyclerView!!.layoutManager = LinearLayoutManager(this)
        adapter_ing!!.updateList(mBuyUsdtListData)

        recyclerView!!.adapter = adapter_ing

        adapter_ing!!.notifyDataSetChanged()

    }

    override fun onResume() {
        super.onResume()
        getList()
    }
    fun getList(){
        transferMoneyViewModel.getUsdtList(this).observe(this, Observer {
            mBuyUsdtListData.clear()
            if (it.code == 0){
                if (it.data!=null){
                    for (datum in it.data) {
                        mBuyUsdtListData.add(datum)

                        adapter_ing!!.notifyDataSetChanged()
                    }

                }
            }
        })
        adapter_ing = IngAdapter(this)

        recyclerView!!.layoutManager = LinearLayoutManager(this)
        adapter_ing!!.updateList(mBuyUsdtListData)

        recyclerView!!.adapter = adapter_ing

        adapter_ing!!.notifyDataSetChanged()
    }


    class IngAdapter(activity: BuyumoneyListActivity) : RecyclerView.Adapter<IngAdapter.ViewHolder>() {
        var bankCardInfoList: ArrayList<BuyUsdtListData.Data>? = null
        var mactivity= activity


        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var bankName: TextView
            var cardNo: TextView
            var time: TextView
            var amount: TextView
            var exrate: TextView
            var usdt: TextView

            var orderno: TextView
            var userName : TextView
            var payName : TextView
            var cancelButton : Button
            var sureButton  : Button


            init {
                bankName = view.findViewById(R.id.bankname)
                cardNo = view.findViewById(R.id.cardno)
                time = view.findViewById(R.id.time)
                amount = view.findViewById(R.id.amount)
                exrate = view.findViewById(R.id.exrate)
                usdt = view.findViewById(R.id.usdt)

                orderno = view.findViewById(R.id.orderno)
                userName = view.findViewById(R.id.username)
                payName = view.findViewById(R.id.payname);
                cancelButton = view.findViewById(R.id.cancel_button)
                sureButton = view.findViewById(R.id.sure_button)


            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.buyusdt_list_item, parent, false)
            return ViewHolder(view)
        }

        @SuppressLint("ResourceAsColor")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val info: BuyUsdtListData.Data = bankCardInfoList!!.get(position)
            val df = DecimalFormat("###.00")

            holder.bankName.text = "卡号:" +  info
            holder.time.text = info.created
            holder.amount.text = "参考价格"+info.score
            holder.exrate.text = "哈希值:"+ info.hashvalue
//            holder.usdt.text = "成交金额USDT:"+ df.format(info.score/mfragment.exrateDouble)

            holder.orderno.text = info.orderno
            holder.userName.text = "实际数量:" + info.scoreusdt
            holder.bankName.text = "汇率:" +info.exgrate
            holder.payName.text = "订单号:" +info.orderno

            holder.cancelButton.setBackgroundColor(R.color.default_color_pressed)
            holder.cancelButton.visibility = View.INVISIBLE
            holder.usdt.visibility = View.INVISIBLE


            when(info.state){
                0 ->{
                    holder.sureButton.text = "待上传"
                }
                1 ->{
                    holder.sureButton.text = "已上传"

                }
                2 ->{
                    holder.sureButton.text = "已完成"

                }
                4 ->{
                    holder.sureButton.text = "已驳回"

                }
                else -> "待上传"

            }


            holder.sureButton.setOnClickListener {
                mactivity.confirmOrder(info.id)
            }

        }

        override fun getItemCount(): Int {
            return bankCardInfoList!!.size
        }

        //更新資料用
        fun updateList(list: ArrayList<BuyUsdtListData.Data>){
            bankCardInfoList = list
        }


    }

    private fun copyToClipboard(str: String) {
        val sdk = Build.VERSION.SDK_INT
        if (sdk < Build.VERSION_CODES.HONEYCOMB) {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.text = str
        } else {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = ClipData.newPlainText("text label", str)
            clipboard.setPrimaryClip(clip)
        }
    }
    fun two(s: String): String {
        return String.format("%.2f", s.toDouble())
    }
    fun confirmOrder(id : String){
        var url : String = PayHelperUtils.getOpenUrl(this) + "USDTUpload/" +id
        ToastManager.showToastCenter(this,url)
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(url)
        startActivity(intent)

        runOnUiThread {


        }


    }


}