package com.jingyu.pay.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tools.payhelper.pay.ui.login.*
import com.tools.payhelper.pay.ui.notifications.UserinfoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    var data = MutableLiveData<UserinfoData>()
    var homeViewModel = LoginDateModel()
    var  token  = MutableLiveData<LoginData>()
    var  update  = MutableLiveData<UpdateData>()
    var version : MutableSharedFlow<UpdateData> = MutableSharedFlow<UpdateData>()
    var _version: MutableSharedFlow<UpdateData>  = version
    init {
        getUpdate()
    }


    fun getUserToken(context: Context,loginid:String,password:String,code:String) : LiveData<LoginData>{
        homeViewModel.setUserLogin(context,loginid,password,code, object : LoginDateModel.LoginrResponse {
            override fun getResponse(s: String) {
                if (!s.isEmpty()){
                    viewModelScope.launch {
                        var userData = Gson().fromJson(s, LoginData::class.java)
                        token.value = userData

                    }
                }

            }

        })

        return  token;
    }

//    fun getGoolge() :{
//        homeViewModel.getGoogle(object : LoginDateModel.LoginrResponse {
//            override fun getResponse(s: String) {
//                viewModelScope.launch {
//                    if (!s.isEmpty()){
//                        var googleData = Gson().fromJson(s,GoogleData::class.java)
//                        if (googleData!=null){
//
//                        }
//                    }
//                }
//            }
//
//        })
//    }


//    fun getUpdate() :LiveData<UpdateData>{
//        homeViewModel.getUpdate(object : LoginDateModel.LoginrResponse {
//            override fun getResponse(s: String) {
//                viewModelScope.launch {
//                    if (!s.isEmpty()){
//                        Log.d("Jack",s)
//                        var userData = Gson().fromJson(s, UpdateData::class.java)
//
//                        update.value = userData
//                    }
//                }
//            }
//
//        })
//        return  update
//    }

     @SuppressLint("SuspiciousIndentation")
     fun getUpdate(){
        viewModelScope.launch {
            homeViewModel.getUpdate().flowOn(Dispatchers.IO).catch {
                Log.d("Jack",it.localizedMessage)
            }.filter {
                !it.isEmpty()
            }.collect {
                Log.d("Jack",it)
                if (!it.isEmpty()){
            var userData = Gson().fromJson(it, UpdateData::class.java)
                    version.emit(userData)

                }




            }
        }

    }

    fun getUserInfo(context: Context) : LiveData<UserinfoData>{
        homeViewModel.getUserinfo(context, object : LoginDateModel.LoginrResponse {
            override fun getResponse(s: String) {

                if (!s.isEmpty()){
                    viewModelScope.launch {
                        var ud = Gson().fromJson(s,UserinfoData::class.java)
                        data.value = ud


                    }
                }
            }

        })
        return  data
    }

    fun  getVersionUpdate(context: Context): LiveData<UpdateData>{
        homeViewModel.getVersionUpdate(context, object : LoginDateModel.LoginrResponse {
            override fun getResponse(s: String) {
                if (!s.isEmpty()){
                    viewModelScope.launch {
                        var up = Gson().fromJson(s,UpdateData::class.java);
                        update.value = up

                    }
                }
            }

        })
        return  update
    }



}