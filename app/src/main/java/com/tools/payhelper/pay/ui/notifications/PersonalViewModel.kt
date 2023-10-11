package com.jingyu.pay.ui.notifications

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tools.payhelper.pay.PayHelperUtils
import com.tools.payhelper.pay.ui.notifications.UserinfoData
import kotlinx.coroutines.launch

class PersonalViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    var homeViewModel = PersonalDateModel()
    var  userinfoData = MutableLiveData<UserinfoData>()


    fun  get(context: Context) : LiveData<UserinfoData>{

        homeViewModel.test(context,PayHelperUtils.getUserToken(context), object : PersonalDateModel.OrderResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    if(!s.isEmpty()){
                        var userData = Gson().fromJson(s, UserinfoData::class.java)
                        userinfoData.value = userData
                    }

                }
            }

            override fun getFailure(s: String) {
            }

        })
        return  userinfoData;
    }

}