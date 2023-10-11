package com.jingyu.pay.ui.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tools.payhelper.R
import com.tools.payhelper.databinding.FragmentDashboardBinding
import com.tools.payhelper.pay.ConfirmOrderDialog
import com.tools.payhelper.pay.PayHelperUtils
import com.tools.payhelper.pay.ui.dashboard.SellListData


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    var adapter: Adapter? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var  switch: Switch

    val sellViewModel: SellViewModel by lazy {
        ViewModelProvider(this, SellViewModelFactory()).get(SellViewModel::class.java)
    }
    var buyDataList: ArrayList<SellListData.Data> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val recyclerView: RecyclerView =  root.findViewById(R.id.recycler_view)
        switch =  root.findViewById(R.id.switch1);
        checkOpen()

        switch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            val ischeckString = if (b) "卖币接单中" else "卖币暂停接单"
            switch.setText(ischeckString)

            if (b){
                openSell()
                PayHelperUtils.saveSellState(requireActivity(),true)


            }else{
                closeSell()
                PayHelperUtils.saveSellState(requireActivity(),false)


            }
        })


        adapter = Adapter(this)

        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        adapter!!.updateList(buyDataList)

        recyclerView!!.adapter = adapter

        adapter!!.notifyDataSetChanged()

        return root
    }

    fun getList(){
        sellViewModel.getSellList(requireActivity()).observe(requireActivity(), Observer {
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
    fun checkOpen(){
        var b = PayHelperUtils.getSellState(requireActivity())
        switch.isChecked = b
        if (b){
            openSell()

        }else{
            closeSell()
        }
    }
    fun openSell(){
        sellViewModel.setSellSetting(requireActivity()).observe(requireActivity(), Observer {
            Log.d("Jack","openSell："+it.msg)


        })
    }

    fun closeSell(){
        sellViewModel.setCloseSellSetting(requireActivity()).observe(requireActivity(), Observer {
            Log.d("Jack","closeSell："+it.msg)

        })
    }
    fun cancelToUrl(id : String){

        var url : String = PayHelperUtils.getOpenUrl(requireActivity()) + "voucher/" +id +"?actionName=cancel"
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(url)
        startActivity(intent)


    }
    fun confirmOrder(data: SellListData.Data){

            val dialog = ConfirmOrderDialog(requireActivity(),data)
            dialog.setAddBankCallback {
                if (it != null){
                    if (it.code == 1){
                        requireActivity().runOnUiThread {
                            getList()
                            dialog.dismiss()
                            Toast.makeText(requireActivity(),"业务操作已完成",Toast.LENGTH_SHORT).show()

                        }
                    }else{
                        requireActivity().runOnUiThread {
                            getList()
                            dialog.dismiss()
                        }
                    }

                }

            }
            dialog.show()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        super.onStop()

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        getList()


    }

    class Adapter(fragment: DashboardFragment) : RecyclerView.Adapter<Adapter.ViewHolder>() {
        var bankCardInfoList:ArrayList<SellListData.Data>? = null
        var mfragment= fragment


        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var bankName: TextView
            var cardNo: TextView
            var time: TextView
            var amount: TextView
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
                orderno = view.findViewById(R.id.orderno)
                userName = view.findViewById(R.id.username)
                payName = view.findViewById(R.id.payname);
                cancelButton = view.findViewById(R.id.cancel_button)
                sureButton = view.findViewById(R.id.sure_button)


            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.sell_list_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val info: SellListData.Data = bankCardInfoList!!.get(position)

            holder.bankName.text = "收款银行:" + info.bankName
//            holder.cardNo.text = info.cardId
            holder.time.text = info.created
            holder.amount.text = "￥"+info.score
            holder.orderno.text = info.orderNo
            holder.userName.text = "收款人姓名:" + info.userName
            holder.payName.text = "打款人姓名:" + info.payUserName
//            holder.addButton.text = info.state

            holder.cancelButton.setOnClickListener {
                mfragment.cancelToUrl(info.id);
            }

            holder.sureButton.setOnClickListener {
                mfragment.confirmOrder(info)
            }




        }

        override fun getItemCount(): Int {
            return bankCardInfoList!!.size
        }

        //更新資料用
        fun updateList(list:ArrayList<SellListData.Data>){
            bankCardInfoList = list
        }
    }
}