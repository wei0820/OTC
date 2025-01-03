package com.jingyu.pay.ui.home

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationChannel.DEFAULT_CHANNEL_ID
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE

import android.content.Intent
import android.media.SoundPool
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tools.payhelper.R

import com.tools.payhelper.pay.AddBuySettingDilog
import com.tools.payhelper.pay.PayHelperUtils
import com.tools.payhelper.pay.ToastManager
import com.tools.payhelper.pay.ui.home.BuyData
import com.tools.payhelper.pay.ui.order.PaymentMatchingData
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import com.tools.payhelper.databinding.FragmentHomeBinding
import com.tools.payhelper.pay.AppManagerViewModel
import com.tools.payhelper.ui.login.AppManagerViewModelFactory


class HomeFragment : Fragment() ,Handler.Callback{
    private var spool: SoundPool? = null
    private var sourceid = 0
    private var _binding: FragmentHomeBinding? = null
    var adapter: BuyAdapter? = null
    var adapter_ing: IngAdapter? = null

    var buyDataList: ArrayList<BuyData.Data> = ArrayList()
    var buyIDDataList: ArrayList<String> = ArrayList()

    var IngDataList: ArrayList<PaymentMatchingData.Data> = ArrayList()

    lateinit var group : RadioGroup
    lateinit var recyclerView: RecyclerView
     var exrateDouble : Double = 7.5

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var fab: FloatingActionButton
     var  isIng : Boolean = false

    val merchantOrdersViewModel: HomeViewModel by lazy {
        ViewModelProvider(this, HomeViewModelFactory()).get(HomeViewModel::class.java)
    }

    val appManagerViewModel : AppManagerViewModel by lazy{
        ViewModelProvider(this,AppManagerViewModelFactory()).get(AppManagerViewModel::class.java)
    }
    var handler: Handler? = null

    lateinit var   spinner : Spinner;
    var  channelId : String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        handler = Handler(this)

        fab = root.findViewById(R.id.normalFAB)
        recyclerView =  root.findViewById(R.id.recyclerView)
        group =root.findViewById(R.id.group_radio)

        group.check(R.id.rb_yestday)
        group.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.rb_yestday ->{
                    spinner.visibility = View.VISIBLE
                    getBuyList()
                    fab.show()

                }

                R.id.rb_today ->{
                    spinner.visibility = View.GONE
                    fab.hide()
                    getinglIst()
                }


            }


        }



        fab.setOnClickListener {
            val dialog = AddBuySettingDilog(activity)
            dialog.setAddBankCallback {
                if (it != null){
                    if (it.code == 0){
                        requireActivity().runOnUiThread {
                            ToastManager.showToastCenter(requireActivity(),it.msg)
                            getBuyList()
                            dialog.dismiss()
                        }
                    }
                }

            }
            dialog.show()
        }


        setBuySetting()
        getBuyList()
         channelId = getString(R.string.app_name)


        spinner = root.findViewById(R.id.spinner)
        val adapter = ArrayAdapter.createFromResource(requireActivity(),
            R.array.spinner_buy,
            android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(0, false)
        spinner.onItemSelectedListener = spnOnItemSelected


        // make the adapter the data set changed for the recycler view

        // now creating the scroll listener for the recycler view
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
                    if(!isIng){
                        fab.show()

                    }
                }

                // of the recycler view is at the first
                // item always show the FAB
                if (!recyclerView.canScrollVertically(-1)) {
                    if(!isIng){
                        fab.show()

                    }                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == 0){
                    fab.show()
                }

            }
        })
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        getInfo()
        getExrate()


    }
    fun  getBuyList(){

        merchantOrdersViewModel.getBuyDataList(requireActivity()).observe(requireActivity(),
            Observer {
                buyDataList.clear()
                buyIDDataList.clear()
                if (it.code == 0){
                    if (it.data!=null){

                        for (datum in it.data) {
                            buyDataList.add(datum)
                            buyIDDataList.add(datum.id)
                            adapter!!.notifyDataSetChanged()

                        }
                        if (buyDataList.size<=0){
                        }
                    }
                }
            })
        adapter = BuyAdapter(this)

        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        adapter!!.updateList(buyDataList)

        recyclerView!!.adapter = adapter

        adapter!!.notifyDataSetChanged()


        handler!!.sendEmptyMessageDelayed(1,60000)

        isIng = false
        fab.show()
    }



    fun  getBuyList(type :String){

        merchantOrdersViewModel.getBuyDataList(requireActivity()).observe(requireActivity(),
            Observer {
                buyDataList.clear()
                buyIDDataList.clear()
                if (it.code == 0){
                    if (it.data!=null){

                        for (datum in it.data) {
                            if (type=="全部"){
                                isIng = false
                                buyDataList.add(datum)
//                                buyIDDataList.add(datum.id)
                                adapter!!.notifyDataSetChanged()
                            }else{
                                when(type){
                                    "支付宝" ->
                                        if (datum.ordertype.equals("JFB")){
                                            buyDataList.add(datum)
                                            adapter!!.notifyDataSetChanged()
                                        }
                                        "银行卡" ->
                                            if (datum.ordertype.equals("BANK")){
                                                buyDataList.add(datum)
                                                adapter!!.notifyDataSetChanged()
                                            }
                                }
                            }


                        }

                    }
                }
            })
        adapter = BuyAdapter(this)

        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        adapter!!.updateList(buyDataList)

        recyclerView!!.adapter = adapter

        adapter!!.notifyDataSetChanged()

        isIng = true
        fab.show()
        handler!!.sendEmptyMessageDelayed(1,30000)
    }

    override fun onPause() {
        super.onPause()
    }

    fun getExrate(){
        merchantOrdersViewModel.getExrateData(requireActivity()).observe(viewLifecycleOwner,
            Observer {
                if (it!=null){
                    if (it.data!=null){
                        exrateDouble = it.data.exrateDoubel
                    }
                }

            })
    }


    fun getinglIst(){
        merchantOrdersViewModel.getPaymentMatching(requireActivity()).observe(requireActivity(),
            Observer {
                IngDataList.clear()
                if (it.code == 0){
                    if (it.data!=null){
                        for (datum in it.data) {
                            IngDataList.add(datum)

                            adapter_ing!!.notifyDataSetChanged()
                        }
                        if (IngDataList.size<=0){
                            adapter_ing!!.notifyDataSetChanged()

                        }
                    }
                }
            })

        adapter_ing = IngAdapter(this)

        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        adapter_ing!!.updateList(IngDataList)

        recyclerView!!.adapter = adapter_ing

        adapter_ing!!.notifyDataSetChanged()
        fab.hide()
        isIng = true
        handler!!.sendEmptyMessageDelayed(1,10000)





    }
    fun setBuySetting(){

        val maxString =
            if (PayHelperUtils.getBuyMax(activity).isEmpty()) "99999" else PayHelperUtils.getBuyMax(
                activity
            )
        val minString =
            if (PayHelperUtils.getBuyMin(activity).isEmpty()) "1" else PayHelperUtils.getBuyMin(
                activity
            )

        merchantOrdersViewModel.getBuySetting(requireActivity(),minString,maxString).observe(requireActivity(), Observer {

        })
    }


    fun  getPament(id:String){



        ToastManager.showToastCenter(requireActivity(),id);

        merchantOrdersViewModel.getPaymentMatchingData(requireActivity(),id).observe(requireActivity(),
            Observer {
                viewLifecycleOwner.lifecycleScope.launch {
                    if (it!=null){
                        if (it.code==0){
                            ToastManager.showToastCenter(requireActivity(),it.msg);
                            getBuyList()
                        }else{
                            ToastManager.showToastCenter(requireActivity(),it.msg);
                            getBuyList()



                        }

                    }else{
                        ToastManager.showToastCenter(requireActivity(),"error");

                    }
                }

            })
    }



    fun cancelToUrl(id : String){
        var url : String = PayHelperUtils.getOpenUrl(requireActivity()) + "voucherError/" +id
        ToastManager.showToastCenter(requireActivity(),url)
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(url)
        startActivity(intent)
        requireActivity().runOnUiThread {
            getinglIst()
            fab.hide()

        }

    }

    fun confirmOrder(id : String){
        var url : String = PayHelperUtils.getOpenUrl(requireActivity()) + "index/" +id
        ToastManager.showToastCenter(requireActivity(),url)
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(url)
        startActivity(intent)

          requireActivity().runOnUiThread {
            getinglIst()
            fab.hide()

        }


    }

    fun getInfo(){
        merchantOrdersViewModel.getUserInfo(requireActivity()).observe(viewLifecycleOwner, Observer {
            if (it!=null){
                requireActivity().runOnUiThread {
                    if (!it.data.isEnable){
                        ToastManager.showToastCenter(requireActivity(),"令牌失效 请重新登入")
                    }
                }

            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    class BuyAdapter(fragment: HomeFragment) : RecyclerView.Adapter<BuyAdapter.ViewHolder>() {
        var bankCardInfoList:ArrayList<BuyData.Data>? = null
        var mfragment= fragment


        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var bankName: TextView
            var cardNo: TextView
            var time: TextView

            var amount: TextView
            var exrate: TextView
            var usdt: TextView
            var orderno: TextView
            var addButton : Button
            var username : TextView


            init {
                bankName = view.findViewById(R.id.bankname)
                cardNo = view.findViewById(R.id.cardno)
                time = view.findViewById(R.id.time)
                amount = view.findViewById(R.id.amount)
                exrate = view.findViewById(R.id.exrate)
                usdt = view.findViewById(R.id.usdt)
                orderno = view.findViewById(R.id.orderno)
                addButton = view.findViewById(R.id.addbtn);
                username = view.findViewById(R.id.username);


            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.buy_list_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val info: BuyData.Data = bankCardInfoList!!.get(position)
            val df = DecimalFormat("###.00")
            holder.bankName.text = info.bankName
            holder.cardNo.text = ""
            holder.time.text = ""
            holder.amount.text = "￥"+info.score
            holder.exrate.text = "单价:"+ mfragment.exrateDouble
            holder.usdt.text = "成交金额USDT:"+ df.format(info.score/mfragment.exrateDouble)
            holder.orderno.text = info.orderNo
            holder.addButton.text = info.state
            holder.username.text =  info.userName

            holder.bankName.setOnClickListener {

                PayHelperUtils.copyToClipboard(mfragment.requireActivity(),info.bankName+"_"+info.subName)
            }

            holder.cardNo.setOnClickListener {

                PayHelperUtils.copyToClipboard(mfragment.requireActivity(),info.cardId)
            }

            holder.amount.setOnClickListener {

                PayHelperUtils.copyToClipboard(mfragment.requireActivity(),"￥"+info.score)
            }

            holder.username.setOnClickListener {

                PayHelperUtils.copyToClipboard(mfragment.requireActivity(),info.userName)
            }

            holder.addButton.setOnClickListener {
                mfragment.getPament(info.id);

            }


        }

        override fun getItemCount(): Int {
            return bankCardInfoList!!.size
        }

        //更新資料用
        fun updateList(list:ArrayList<BuyData.Data>){
            bankCardInfoList = list
        }
    }


    class IngAdapter(fragment: HomeFragment) : RecyclerView.Adapter<IngAdapter.ViewHolder>() {
        var bankCardInfoList: java.util.ArrayList<PaymentMatchingData.Data>? = null
        var mfragment= fragment


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
                LayoutInflater.from(parent.context).inflate(R.layout.paymentmatching_list_item, parent, false)
            return ViewHolder(view)
        }

        @SuppressLint("ResourceAsColor")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val info: PaymentMatchingData.Data = bankCardInfoList!!.get(position)
            val df = DecimalFormat("###.00")

            holder.bankName.text = "卡号:" +  info.cardId
            holder.time.text = info.created
            holder.amount.text = "￥"+info.score
            holder.exrate.text = "单价:"+ mfragment.exrateDouble
            holder.usdt.text = "成交金额USDT:"+ df.format(info.score/mfragment.exrateDouble)

            holder.orderno.text = info.orderNo
            holder.userName.text = "收款人姓名:" + info.userName
            holder.payName.text = "收款银行:" +info.bankName
            holder.cancelButton.setBackgroundColor(R.color.default_color_pressed)

            holder.payName.setOnClickListener {

                PayHelperUtils.copyToClipboard(mfragment.requireActivity(),info.bankName+"_"+info.subName)
            }

            holder.bankName.setOnClickListener {

                PayHelperUtils.copyToClipboard(mfragment.requireActivity(),info.cardId)
            }

            holder.amount.setOnClickListener {

                PayHelperUtils.copyToClipboard(mfragment.requireActivity(),"￥"+info.score)
            }

            holder.userName.setOnClickListener {

                PayHelperUtils.copyToClipboard(mfragment.requireActivity(),info.userName)
            }



            holder.cancelButton.setOnClickListener {
                mfragment.cancelToUrl(info.id);
            }

            holder.sureButton.setOnClickListener {
                mfragment.confirmOrder(info.id)
            }

        }

        override fun getItemCount(): Int {
            return bankCardInfoList!!.size
        }

        //更新資料用
        fun updateList(list: java.util.ArrayList<PaymentMatchingData.Data>){
            bankCardInfoList = list
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        if (handler != null) {
            handler!!.removeCallbacksAndMessages(null);
            handler = null;

        }
    }


    override fun handleMessage(p0: Message): Boolean {
        if (p0.what ==1){
            if (!isIng){
                requireActivity().runOnUiThread {
                    ToastManager.showToastCenter(requireActivity(),"买币資料刷新")
                }
                spinner.setSelection(0)
                getBuyList()
            }else{
                requireActivity().runOnUiThread {
                    ToastManager.showToastCenter(requireActivity(),"进行中订单/买币資料刷新")
                }
                getinglIst()

            }

        }
        return false;
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
//                getSelectList(dateString,sInfo)
                getBuyList(sInfo)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }

}