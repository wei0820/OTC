package com.jingyu.pay.ui.group

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tools.payhelper.pay.ui.group.GroupListData
import com.tools.payhelper.pay.ui.group.ReportDayData
import com.tools.payhelper.pay.ui.group.ReportsData
import com.tools.payhelper.pay.ui.group.ReportsTeamData
import kotlinx.coroutines.launch

class GroupViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    var groupDateModel = GroupDateModel()
    var groupListData = MutableLiveData<GroupListData>()
    var groupRepostData = MutableLiveData<ReportsData>()
    var mReportsTeamData = MutableLiveData<ReportsTeamData>()
    var mReportDayData = MutableLiveData<ReportDayData>()



    fun getGroupList(context: Context) : LiveData<GroupListData>{
        groupDateModel.getGroupTimeList(context, object : GroupDateModel.GroupResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    if(!s.isEmpty()){
                        var data = Gson().fromJson(s,GroupListData::class.java)
                        if (data !=null){
                            groupListData.value = data
                        }
                    }
                }


            }

        })
        return groupListData;
    }

    fun getReport(context: Context, id : String,day : String) : LiveData<ReportsData>{

        groupDateModel.getGroupReportsList(context,id,day, object :GroupDateModel.GroupResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    if (!s.isEmpty()){
                        var data = Gson().fromJson(s,ReportsData::class.java)
                        if (data != null){
                            groupRepostData.value = data

                        }
                    }
                }
            }

        })


        return  groupRepostData
    }

    fun getReportTime(context: Context, id : String,day : String) : LiveData<ReportsTeamData>{

        groupDateModel.getGroupReportsTeamList(context,id,day, object :GroupDateModel.GroupResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    if (!s.isEmpty()){
                        var data = Gson().fromJson(s,ReportsTeamData::class.java)
                        if (data != null){
                            mReportsTeamData.value = data

                        }
                    }
                }
            }


        })
        return mReportsTeamData;
    }

    fun getGroupReportDay(context: Context):LiveData<ReportDayData>{

        groupDateModel.getGroupReportDay(context, object :GroupDateModel.GroupResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    if (!s.isEmpty()){
                        var data = Gson().fromJson(s,ReportDayData::class.java)
                        if (data !=null){
                            mReportDayData.value = data
                        }
                    }
                }
            }

        })



        return mReportDayData;

    }

}