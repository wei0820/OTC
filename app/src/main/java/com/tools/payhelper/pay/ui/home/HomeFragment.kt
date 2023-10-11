package com.jingyu.pay.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tools.payhelper.R

import com.tools.payhelper.databinding.FragmentHomeBinding
import com.tools.payhelper.pay.AddBuySettingDilog
import com.tools.payhelper.pay.PayHelperUtils
import com.tools.payhelper.pay.ToastManager
import com.tools.payhelper.pay.ui.home.BuyData

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    var adapter: Adapter? = null
    var buyDataList: ArrayList<BuyData.Data> = ArrayList()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val merchantOrdersViewModel: HomeViewModel by lazy {
        ViewModelProvider(this, HomeViewModelFactory()).get(HomeViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val fab: FloatingActionButton = root.findViewById(R.id.normalFAB)
        val recyclerView: RecyclerView =  root.findViewById(R.id.recyclerView)

        setBuySetting()

        getBuyDataList()
        fab.setOnClickListener {
            val dialog = AddBuySettingDilog(activity)
            dialog.setAddBankCallback {
                if (it != null){
                    if (it.code == 0){
                        requireActivity().runOnUiThread {
                            getBuyDataList()
                            dialog.dismiss()
                        }
                    }
                }

            }
            dialog.show()
        }


        adapter = Adapter(this)

        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        adapter!!.updateList(buyDataList)

        recyclerView!!.adapter = adapter

        adapter!!.notifyDataSetChanged()

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
        return root
    }

    fun setBuySetting(){
        val maxString =
            if (PayHelperUtils.getBuyMax(activity).isEmpty()) "1000" else PayHelperUtils.getBuyMax(
                activity
            )
        val minString =
            if (PayHelperUtils.getBuyMin(activity).isEmpty()) "300" else PayHelperUtils.getBuyMin(
                activity
            )

        merchantOrdersViewModel.getBuySetting(requireActivity(),minString,maxString).observe(requireActivity(), Observer {

        })
    }


    fun getBuyDataList(){
        merchantOrdersViewModel.getBuyDataList(requireActivity()).observe(requireActivity(),
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

    fun  getPament(id:String){
        merchantOrdersViewModel.getPaymentMatchingData(requireActivity(),id).observe(requireActivity(),
            Observer {
                if (it!=null){
                    ToastManager.showToastCenter(requireActivity(),it.msg);
                    getBuyDataList()
                }
            })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    class Adapter(fragment: HomeFragment) : RecyclerView.Adapter<Adapter.ViewHolder>() {
        var bankCardInfoList:ArrayList<BuyData.Data>? = null
        var mfragment= fragment


        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var bankName: TextView
            var cardNo: TextView
            var time: TextView
            var amount: TextView
            var orderno: TextView
            var addButton : Button


            init {
                bankName = view.findViewById(R.id.bankname)
                cardNo = view.findViewById(R.id.cardno)
                time = view.findViewById(R.id.time)
                amount = view.findViewById(R.id.amount)
                orderno = view.findViewById(R.id.orderno)
                addButton = view.findViewById(R.id.addbtn);


            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.buy_list_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val info: BuyData.Data = bankCardInfoList!!.get(position)

            holder.bankName.text = info.bankName
            holder.cardNo.text = info.cardId
            holder.time.text = info.created
            holder.amount.text = "￥"+info.score
            holder.orderno.text = info.orderNo
            holder.addButton.text = info.state
            if (info.state.equals("已接单")){
                holder.addButton.isEnabled = false
            }else{
                holder.addButton.setOnClickListener {
                    mfragment.getPament(info.id);

                }
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
}