package com.jingyu.pay.ui.order

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.tools.payhelper.R
import com.tools.payhelper.databinding.FragmentOrderBinding
import com.tools.payhelper.pay.PayHelperUtils
import com.tools.payhelper.pay.ui.order.PaymentMatchingData
import java.util.*

class OrderFragment : Fragment(){

    private var _binding: FragmentOrderBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val merchantOrdersViewModel: OrderViewModel by lazy {
        ViewModelProvider(this, OrderViewModelFactory()).get(OrderViewModel::class.java)
    }
    var adapter:Adapter? = null

    var buyDataList: ArrayList<PaymentMatchingData.Data> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val recyclerView: RecyclerView =  root.findViewById(R.id.recycler_view)

        getList();


        adapter = Adapter(this)

        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        adapter!!.updateList(buyDataList)

        recyclerView!!.adapter = adapter

        adapter!!.notifyDataSetChanged()



        return root
    }

    override fun onResume() {
        super.onResume()
        getList()
    }

    fun getList(){
        merchantOrdersViewModel.getPaymentMatching(requireActivity()).observe(requireActivity(),
            Observer {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun cancelToUrl(id : String){

        var url : String = PayHelperUtils.getOpenUrl(requireActivity()) + "voucherError/" +id

        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(url)
        startActivity(intent)


    }

    fun confirmOrder(id : String){

        var url : String = PayHelperUtils.getOpenUrl(requireActivity()) + "index/" +id
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(url)
        startActivity(intent)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

class Adapter(fragment: OrderFragment) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    var bankCardInfoList:ArrayList<PaymentMatchingData.Data>? = null
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
            LayoutInflater.from(parent.context).inflate(R.layout.paymentmatching_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info: PaymentMatchingData.Data = bankCardInfoList!!.get(position)

        holder.bankName.text = "卡号:" +  info.cardId
//            holder.cardNo.text = info.cardId
        holder.time.text = info.created
        holder.amount.text = "￥"+info.score
        holder.orderno.text = info.orderNo
        holder.userName.text = "收款人姓名:" + info.userName
        holder.payName.text = "收款银行:" +info.bankName
//            holder.addButton.text = info.state

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
    fun updateList(list:ArrayList<PaymentMatchingData.Data>){
        bankCardInfoList = list
    }


}