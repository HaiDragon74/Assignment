package com.example.assignment.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment.Model.ProfileUser
import com.example.assignment.retrofil.RetrofitUser
import com.example.assignment.room.Database
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelProfileUser(private val database: Database):ViewModel() {
    private var mutableLiveDataProfileUser= MutableLiveData<ProfileUser>()
    private var mutableLiveDataRealProfile= database.daoProfile().realProfile()


    fun getProfileUser(id:String){
        RetrofitUser.retrofitUser.getProfileUser(id).enqueue(object : Callback<ProfileUser> {
            override fun onResponse(call: Call<ProfileUser>, response: Response<ProfileUser>) {
                if (response.body()!=null){
                    val profileUser=response.body()!!
                    mutableLiveDataProfileUser.value=profileUser
                }
            }
            override fun onFailure(call: Call<ProfileUser>, t: Throwable) {
                Log.d("onFailurelistUserProfile",t.message.toString())
            }
        })
    }
    fun obsProfileUser(): LiveData<ProfileUser> {
        return mutableLiveDataProfileUser
    }
    fun insertProfile(profileUser: ProfileUser){
        viewModelScope.launch {
            database.daoProfile().insertProfile(profileUser)
        }
    }
    fun realProfile():LiveData<List<ProfileUser>>{
        return mutableLiveDataRealProfile
    }
    fun updaterProfile(profileUser: ProfileUser){
        viewModelScope.launch {
            database.daoProfile().updaterProfile(profileUser)
        }
    }
    fun deleteProfile(profileUser: ProfileUser){
        viewModelScope.launch {
            database.daoProfile().deleteProfile(profileUser)
        }
    }
}