package com.jingyu.pay.ui.dashboard

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.tools.payhelper.pay.ToastManager
import com.tools.payhelper.pay.ui.dashboard.CollectionQueueData
import com.tools.payhelper.pay.ui.dashboard.CollectionQueueOffData
import com.tools.payhelper.pay.ui.dashboard.ConfirmData
import com.tools.payhelper.pay.ui.dashboard.SellListData
import com.tools.payhelper.pay.ui.home.ExrateData
import com.tools.payhelper.pay.ui.notifications.UserinfoData
import kotlinx.coroutines.launch

class SellViewModel : ViewModel() {

    private val gson = Gson()
    private val TAG = "SellViewModel_Debug"

    var exrateData = MutableLiveData<ExrateData>()
    var data = MutableLiveData<UserinfoData>()
    var sellDateModel = SellDateModel()
    var setSellSettingData = MutableLiveData<CollectionQueueData>()
    var setCloseSellSettingData = MutableLiveData<CollectionQueueOffData>()
    var mSellListData = MutableLiveData<SellListData>()
    var confirmData = MutableLiveData<ConfirmData>()

    // --- 核心解析工具 (附帶證據顯示功能) ---

    private inline fun <reified T> parseJson(context: Context, s: String, onSuccess: (T) -> Unit) {
        if (s.isBlank()) {
            ToastManager.showToastCenter(context, "伺服器回傳內容為空")
            return
        }
        try {
            val result = gson.fromJson(s, T::class.java)
            if (result != null) {
                onSuccess(result)
            }
        } catch (e: JsonSyntaxException) {
            // 抓出後端亂吐的內容前 50 字作為證據
            val evidence = if (s.length > 50) s.substring(0, 50) + "..." else s
            Log.e(TAG, "解析失敗! 原始內容: $s")
            ToastManager.showToastCenter(context, "解析失敗! 原始內容: $evidence")
        } catch (e: Exception) {
            Log.e(TAG, "發生意外錯誤: ${e.message}")
            ToastManager.showToastCenter(context, "系統錯誤: ${e.message}")
        }
    }

    // --- 各項 API 調用邏輯 ---

    fun setSellSetting(context: Context): LiveData<CollectionQueueData> {
        sellDateModel.setSellSetting(context, object : SellDateModel.SellResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    parseJson<CollectionQueueData>(context, s) { setSellSettingData.value = it }
                }
            }
        })
        return setSellSettingData
    }

    fun setCloseSellSetting(context: Context): LiveData<CollectionQueueOffData> {
        sellDateModel.setCloseSellSetting(context, object : SellDateModel.SellResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    parseJson<CollectionQueueOffData>(context, s) { setCloseSellSettingData.value = it }
                }
            }
        })
        return setCloseSellSettingData
    }

    fun getSellList(context: Context): LiveData<SellListData> {
        sellDateModel.getSellList(context, object : SellDateModel.SellResponse {
            override fun getResponse(s: String) {
                Log.e(TAG, "內容: $s")

                viewModelScope.launch {
                    parseJson<SellListData>(context, s) { mSellListData.value = it }
                }
            }
        })
        return mSellListData
    }

    fun getComfirmOrder(id: String, userName: String, context: Context): LiveData<ConfirmData> {
        sellDateModel.setConfirmOrder(id, userName, context, object : SellDateModel.SellResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    parseJson<ConfirmData>(context, s) { confirmData.value = it }
                }
            }
        })
        return confirmData
    }

    fun getExrateData(context: Context): LiveData<ExrateData> {
        sellDateModel.getExrate(context, object : SellDateModel.SellResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    parseJson<ExrateData>(context, s) { exrateData.value = it }
                }
            }
        })
        return exrateData
    }

    fun getUserInfo(context: Context): LiveData<UserinfoData> {
        sellDateModel.getUserinfo(context, object : SellDateModel.SellResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    parseJson<UserinfoData>(context, s) { data.value = it }
                }
            }
        })
        return data
    }
}