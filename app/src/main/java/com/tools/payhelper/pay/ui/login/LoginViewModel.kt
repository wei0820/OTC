package com.jingyu.pay.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.tools.payhelper.pay.ToastManager
import com.tools.payhelper.pay.ui.dashboard.SellListData
import com.tools.payhelper.pay.ui.login.*
import com.tools.payhelper.pay.ui.notifications.UserinfoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val gson = Gson()
    private val TAG = "LoginViewModel_Debug"

    var data = MutableLiveData<UserinfoData>()
    var homeViewModel = LoginDateModel()
    var token = MutableLiveData<LoginData>()
    var postData = MutableLiveData<PostDBData>()

    var update = MutableLiveData<UpdateData>()
    var version: MutableSharedFlow<UpdateData> = MutableSharedFlow()
    var _version: MutableSharedFlow<UpdateData> = version
    var checkSellDatList = MutableLiveData<SellListData>()

    init {
        getUpdate()
    }

    /**
     * 核心安全解析工具：
     * 1. 防止 JsonSyntaxException 導致閃退
     * 2. 解析失敗時，自動將後端回傳的「爛內容」顯示在 Toast 上當證據
     */
    private inline fun <reified T> safeParse(context: Context, s: String, onSuccess: (T) -> Unit) {
        if (s.isNullOrBlank()) return
        try {
            val result = gson.fromJson(s, T::class.java)
            if (result != null) {
                onSuccess(result)
            }
        } catch (e: JsonSyntaxException) {
            // 抓出後端吐的前 50 個字作為證據
            val evidence = if (s.length > 50) s.substring(0, 50) + "..." else s
            Log.e(TAG, "解析失敗！後端回傳了非 JSON 內容: $s")

            // 直接在螢幕上公審後端同事
            ToastManager.showToastCenter(context, "格式異常：$evidence")
        } catch (e: Exception) {
            Log.e(TAG, "發生意外錯誤: ${e.message}")
        }
    }

    fun getUserToken(context: Context, loginid: String, password: String, code: String): LiveData<LoginData> {
        homeViewModel.setUserLogin(context, loginid, password, code, object : LoginDateModel.LoginrResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    safeParse<LoginData>(context, s) { token.value = it }
                }
            }

            override fun getErrorResponse(s: String) {
                token.postValue(null)
            }
        })
        return token
    }

    fun postDb(context: Context, FromAccount: String, ToAccount: String, Amount: String, FullText: String): LiveData<PostDBData> {
        homeViewModel.postDRMBNotification(context, FromAccount, ToAccount, Amount, FullText,
            object : LoginDateModel.LoginrResponse {
                override fun getResponse(s: String) {
                    viewModelScope.launch {
                        safeParse<PostDBData>(context, s) { postData.value = it }
                    }
                }

                override fun getErrorResponse(s: String) {}
            })
        return postData
    }

    @SuppressLint("SuspiciousIndentation")
    fun getUpdate() {
        viewModelScope.launch {
            homeViewModel.getUpdate()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e(TAG, "Update Flow Error: ${e.message}")
                }
                .filter { it.isNotBlank() }
                .collect { result ->
                    try {
                        val json = result.trim()
                        if (json.startsWith("{")) {
                            val userData = gson.fromJson(json, UpdateData::class.java)
                            version.emit(userData)
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Update Parsing Error")
                    }
                }
        }
    }

    fun getUserInfo(context: Context): LiveData<UserinfoData> {
        homeViewModel.getUserinfo(context, object : LoginDateModel.LoginrResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    safeParse<UserinfoData>(context, s) { data.value = it }
                }
            }

            override fun getErrorResponse(s: String) {}
        })
        return data
    }

    // --- 這裡就是你 2026-03-24 報錯的地方 ---
    fun getCheckList(context: Context): LiveData<SellListData> {
        homeViewModel.getSellDataList(context, object : LoginDateModel.LoginrResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    // 使用安全解析，出錯會跳 Toast 顯示證據
                    safeParse<SellListData>(context, s) { checkSellDatList.value = it }
                }
            }

            override fun getErrorResponse(s: String) {}
        })
        return checkSellDatList
    }
}