package com.example.assignment.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.example.assignment.Model.User
import com.example.assignment.retrofil.RetrofitUser
import com.example.assignment.room.Database
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelUser(val database: Database):ViewModel() {
    private var mutableLiveDataUser=MutableLiveData<List<User>?>()
    private var mutableListRealUser=database.dao().realUser()
    fun getUser(){
        RetrofitUser.retrofitUser.getListUser().enqueue(object :Callback<List<User>>{
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val listUser = response.body()
                    if (listUser != null) {
                        if (mutableLiveDataUser.value.isNullOrEmpty()) {
                            mutableLiveDataUser.value = listUser
                            Log.d("dsadsadsadsa",mutableLiveDataUser.toString())
                        } else {

                        }
                    }
                } else {

                }
            }
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.d("onFailurelistUser",t.message.toString())
            }
        })
    }
    fun obsUser(): LiveData<List<User>?> {
        return mutableLiveDataUser
    }
    fun obsRealUser():LiveData<List<User>>{
        return mutableListRealUser
    }
    fun insertUser(user: User){
        viewModelScope.launch {
            database.dao().insert(user)
        }
    }
    fun updaterUser(user: User){
        viewModelScope.launch {
            database.dao().updaterUser(user)
        }
    }
}