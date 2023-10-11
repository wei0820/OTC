package com.jingyu.pay.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.jingyu.pay.MainActivity
import com.jingyu.pay.ui.accountchange.AccountChangeActivity
import com.jingyu.pay.ui.bankcard.BankCardListActivity
import com.jingyu.pay.ui.buyrecord.BuyRecordActivity
import com.jingyu.pay.ui.group.GroupListctivity
import com.jingyu.pay.ui.group.GroupReportActivity
import com.jingyu.pay.ui.group.ReportDayActivity
import com.jingyu.pay.ui.password.PasswordActivity
import com.jingyu.pay.ui.sellrecord.SellRecordActivity
import com.jingyu.pay.ui.transaction.TransactionActivity
import com.tools.payhelper.R
import com.tools.payhelper.databinding.FragmentNotificationsBinding
import com.tools.payhelper.pay.PayHelperUtils
import com.tools.payhelper.pay.ToastManager
import com.tools.payhelper.pay.ui.bankcard.AddCardDialog
import com.tools.payhelper.pay.ui.login.AddGoogleDialog
import kotlinx.coroutines.launch

class NotificationsFragment : Fragment() ,View.OnClickListener{

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var  layout_bankcard : LinearLayout

    lateinit var  buy_record_layout : RelativeLayout
    lateinit var  sell_record_layout : RelativeLayout
    lateinit var  frozenrecord : RelativeLayout
    lateinit var  account_layou : RelativeLayout

    lateinit var layout_grouplist :RelativeLayout
    lateinit var layout_groupreport : RelativeLayout
    lateinit var banklayout : RelativeLayout
    lateinit var reportday_layout :RelativeLayout
    lateinit var passlayout :RelativeLayout
    lateinit var text1 :TextView
    lateinit var text2:TextView
    lateinit var text3 :TextView
    lateinit var text4 :TextView
    lateinit var text5 :TextView
    lateinit var name :TextView



    val personalViewModel: PersonalViewModel by lazy {
        ViewModelProvider(this, PersonalViewModelFactory()).get(PersonalViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        text1 = root.findViewById(R.id.text1)
        text2 = root.findViewById(R.id.text2)
        text3 = root.findViewById(R.id.text3)
        text4 = root.findViewById(R.id.text4)
        text5 = root.findViewById(R.id.text5)
        name = root.findViewById(R.id.nav_text)
        buy_record_layout  = root.findViewById(R.id.buy_record_layout)
        sell_record_layout  = root.findViewById(R.id.sell_record_layout)
        frozenrecord  = root.findViewById(R.id.frozenrecord)
        account_layou  = root.findViewById(R.id.account_layou)
        layout_grouplist = root.findViewById(R.id.layout_grouplist)
        layout_groupreport = root.findViewById(R.id.layout_groupreport)
        banklayout = root.findViewById(R.id.banklayout)
        reportday_layout = root.findViewById(R.id.reportday_layout)
        passlayout = root.findViewById(R.id.passlayout);
        buy_record_layout.setOnClickListener(this)
        sell_record_layout.setOnClickListener(this)
        frozenrecord.setOnClickListener(this)
        account_layou.setOnClickListener(this)
        layout_grouplist.setOnClickListener(this)

        layout_groupreport.setOnClickListener(this)
        banklayout.setOnClickListener(this)
        reportday_layout.setOnClickListener(this)
        passlayout.setOnClickListener(this)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        personalViewModel.get(requireActivity()).observe(requireActivity(), Observer {

            var array = it.data.apIs.split("|");

            PayHelperUtils.saveOpenUrl(context,array.get(0).toString())

            PayHelperUtils.isShowNews(context,it.data.note)

            PayHelperUtils.saveRebate(context,it.data.rebate.toString())
            PayHelperUtils.savePaymentXeRebate(context,it.data.paymentXeRebate.toString())
            PayHelperUtils.saveAlipayRebate(context,it.data.alipayRebate.toString())
            text1.text = it.data.commission.toString()
            text2.text = it.data.quota.toString()
            text3.text = it.data.frozen.toString()
            text4.text = it.data.payment.toString()
            text5.text = it.data.collection.toString()
            name.text=  PayHelperUtils.getUserName(context)



        })

        getActivityData()




    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.buy_record_layout -> startActivity(Intent().setClass(requireActivity(),BuyRecordActivity::class.java))
            R.id.sell_record_layout ->startActivity(Intent().setClass(requireActivity(),SellRecordActivity::class.java))
            R.id.frozenrecord ->startActivity(Intent().setClass(requireActivity(),TransactionActivity::class.java))
            R.id.account_layou -> startActivity(Intent().setClass(requireActivity(),AccountChangeActivity::class.java))
            R.id.layout_grouplist -> startActivity(Intent().setClass(requireActivity(),GroupListctivity::class.java))
            R.id.layout_groupreport ->startActivity(Intent().setClass(requireActivity(),GroupReportActivity::class.java))
            R.id.banklayout ->startActivity(Intent().setClass(requireActivity(),BankCardListActivity::class.java))
            R.id.reportday_layout->startActivity(Intent().setClass(requireActivity(),ReportDayActivity::class.java))
            R.id.passlayout -> startActivity(Intent().setClass(requireActivity(), PasswordActivity::class.java))
        }
    }

    fun  getActivityData(){
        val activity: MainActivity? = activity as MainActivity?

        var boolean = activity!!.getData()

        if (boolean){
            val dialog = AddGoogleDialog(requireActivity())
            dialog.setAddBankCallback {
                if (it!=null){
                    viewLifecycleOwner.lifecycleScope.launch {
                        if (it.code==0){
                            ToastManager.showToastCenter(requireActivity(),it.msg)
                            dialog.dismiss()

                        }else{
                            ToastManager.showToastCenter(requireActivity(),it.msg)

                        }
                    }
                }
            }
            dialog.show()

        }

    }


}