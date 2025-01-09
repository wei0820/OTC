package com.jingyu.pay.ui.group

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.ClipboardManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tools.payhelper.R
import com.tools.payhelper.pay.ToastManager
import com.tools.payhelper.pay.ui.group.AddGroupDialog
import com.tools.payhelper.pay.ui.group.GroupListData


class GroupListctivity : AppCompatActivity() {
    lateinit var fab: FloatingActionButton
    lateinit  var recyclerView: RecyclerView
    var adapter: Adapter? = null
    var buyDataList: ArrayList<GroupListData.Data> = ArrayList()
    val groupViewModel: GroupViewModel by lazy {
        ViewModelProvider(this, GroupViewModelFactory()).get(GroupViewModel::class.java)
    }
    lateinit var  close : ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_listctivity)
        title = "团队成员"
         fab  = findViewById(R.id.normalFAB)
         recyclerView =  findViewById(R.id.recyclerView)
        close = findViewById(R.id.closeBtn)
        close.setOnClickListener {
            finish()
        }
        getGroupList()
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
            val dialog = AddGroupDialog(this)
            dialog.setAddBankCallback {
                if (it!=null){
                    runOnUiThread {
                        ToastManager.showToastCenter(this,it.msg)
                        getGroupList()

                    }
                }
            }
            dialog.show()
        }


    }

    fun  getGroupList(){
        groupViewModel.getGroupList(this).observe(this, Observer {
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
     fun copyToClipboard(str: String) {
        val sdk = Build.VERSION.SDK_INT
        if (sdk < Build.VERSION_CODES.HONEYCOMB) {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.text = str
            Log.e("version", "1 version")
        } else {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = ClipData.newPlainText("text label", str)
            clipboard.setPrimaryClip(clip)
            Log.e("version", "2 version")
        }
    }

}
class Adapter(activity:GroupListctivity) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    var bankCardInfoList:ArrayList<GroupListData.Data>? = null
    var mActivity = activity


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var bankName: TextView
        var cardNo: TextView
        var time: TextView
        var amount: TextView
        var orderno: TextView
        var frozen : TextView
        var alipayRebate : TextView
        var wechatRebate : TextView
        var alipayXeRebate : TextView
        var wechatXeRebate : TextView
        var drmbRebate : TextView
        var unionRebate : TextView



        init {
            bankName = view.findViewById(R.id.bankname)
            cardNo = view.findViewById(R.id.cardno)
            time = view.findViewById(R.id.time)
            amount = view.findViewById(R.id.amount)
            orderno = view.findViewById(R.id.orderno)
            frozen = view.findViewById(R.id.frozen);
            alipayRebate = view.findViewById(R.id.alipayRebate)
            wechatRebate = view.findViewById(R.id.wechatRebate)
            alipayXeRebate = view.findViewById(R.id.alipayXeRebate)
            wechatXeRebate = view.findViewById(R.id.wechatXeRebate)
            drmbRebate = view.findViewById(R.id.drmbRebate)
            unionRebate = view.findViewById(R.id.unionRebate)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.group_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info: GroupListData.Data = bankCardInfoList!!.get(position)
        holder.orderno.text = "用户名:" +info.loginId
        holder.bankName.text = "电话:"+info.mobile
        holder.time.text = "创建日期:"+info.created

        holder.cardNo.text ="可用余额￥"+ info.quota

        holder.amount.text = "佣金￥"+info.commission

        holder.frozen.text = "进行中金额￥" + info.frozen

        holder.alipayRebate.text = "支付宝佣金￥" + info.alipayRebate

        holder.wechatRebate.text =  "微信佣金￥" + info.wechatrebate

        if (info.drmbRebate==null){
            holder.drmbRebate.text =  "数字人民币佣金￥" +  "0"

        }else{
            holder.drmbRebate.text =  "数字人民币佣金￥" + info.drmbRebate

        }
        if (info.unionRebate==null){
            holder.unionRebate.text =  "银联佣金￥" +  "0"

        }else{
            holder.unionRebate.text =  "银联佣金￥" + info.unionRebate

        }
        if (info.alipayXeRebate!=null){
            holder.alipayXeRebate.text = "小额支付宝佣金￥" + info.alipayXeRebate

        }else{
            holder.alipayXeRebate.text = "小额支付宝佣金￥" +  "0"

        }
        if (info.weChatXeRebate!=null){
            holder.wechatXeRebate.text =  "小额微信佣金￥" + info.weChatXeRebate

        }else{
            holder.wechatXeRebate.text =  "小额微信佣金￥"+  "0"

        }

        holder.orderno.setOnClickListener {
            if (!info.loginId.isEmpty()){
                mActivity.copyToClipboard(info.loginId)
                ToastManager.showToastCenter(mActivity, "复制到剪贴簿用户名:"+info.loginId)

            }

        }

    }

    override fun getItemCount(): Int {
        return bankCardInfoList!!.size
    }

    //更新資料用
    fun updateList(list:ArrayList<GroupListData.Data>){
        bankCardInfoList = list
    }

}
