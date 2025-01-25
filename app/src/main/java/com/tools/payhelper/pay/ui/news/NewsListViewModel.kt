package com.tools.payhelper.pay.ui.news

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jingyu.pay.ui.group.GroupDateModel
import com.tools.payhelper.pay.ui.group.GroupListData
import com.tools.payhelper.pay.ui.group.ReportDayData
import com.tools.payhelper.pay.ui.group.ReportsData
import com.tools.payhelper.pay.ui.group.ReportsTeamData
import com.tools.payhelper.pay.ui.money.TransferData
import kotlinx.coroutines.launch

class NewsListViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    var newsListDateModel = NewsListDateModel()
    var groupListData = MutableLiveData<NewsListData>()



    fun getNewsList(context: Context) : LiveData<NewsListData>{
        newsListDateModel.getNewsLList(context, object :NewsListDateModel.GroupResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    if (!s.isEmpty()){
                        var ud = Gson().fromJson(s, NewsListData::class.java)
                        groupListData.value = ud
                    }
                }
            }

        })

        return groupListData;
    }



}