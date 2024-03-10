package com.example.assignment.retrofil

import com.example.assignment.Model.ProfileUser
import com.example.assignment.Model.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitUser {
    companion object{
        val api=Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val retrofitUser= api.create(RetrofitUser::class.java)
    }
    @GET("users")
    fun getListUser():Call<List<User>>
    @GET("users/{id}")
    fun getProfileUser(@Path("id")id:String):Call<ProfileUser>
}