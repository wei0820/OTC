package com.jingyu.pay.ui.notifications

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.tools.payhelper.pay.ToastManager
import com.tools.payhelper.pay.ui.notifications.UserinfoData
import kotlinx.coroutines.launch

class PersonalViewModel : ViewModel() {

    private val gson = Gson()
    private val TAG = "PersonalVM_Debug"

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    var homeViewModel = PersonalDateModel()
    var userinfoData = MutableLiveData<UserinfoData>()

    /**
     * 安全解析工具：遇到非 JSON 內容（例如 HTML 錯誤頁面）時，
     * 會彈出 Toast 顯示前 50 個字，讓爛同事無所遁形。
     */
    private inline fun <reified T> safeParse(context: Context, s: String, onSuccess: (T) -> Unit) {
        if (s.isNullOrBlank()) return
        try {
            val result = gson.fromJson(s, T::class.java)
            if (result != null) {
                onSuccess(result)
            }
        } catch (e: JsonSyntaxException) {
            // 抓包後端吐的髒資料
            val evidence = if (s.length > 50) s.substring(0, 50) + "..." else s
            Log.e(TAG, "解析失敗，內容：$s")
            // 直接彈出證據
            ToastManager.showToastCenter(context, "格式異常：$evidence")
        } catch (e: Exception) {
            Log.e(TAG, "意外錯誤：${e.message}")
        }
    }

    /**
     * 獲取用戶資訊
     * 修正重點：參數從 (context, token, object) 簡化為 (context, object)
     */
    fun get(context: Context): LiveData<UserinfoData> {
        // 因為 PersonalDateModel 內部已經會自動抓 token 了，所以這裡把 token 參數移除
        homeViewModel.test(context, object : PersonalDateModel.OrderResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    if (s.isNotBlank()) {
                        Log.d(TAG, "收到 API 回傳：$s")

                        // 使用安全解析，出錯自動跳 Toast
                        safeParse<UserinfoData>(context, s) { userData ->
                            userinfoData.value = userData
                        }
                    }
                }
            }

            override fun getFailure(s: String) {
                Log.e(TAG, "請求失敗：$s")
                viewModelScope.launch {
                    // 如果網路斷掉或伺服器噴 500，也會直接提示
                    ToastManager.showToastCenter(context, "連線失敗：$s")
                }
            }
        })
        return userinfoData
    }
}