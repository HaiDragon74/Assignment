package com.example.assignment.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment.Model.User
import com.example.assignment.adapter.AdapterUser
import com.example.assignment.databinding.ActivityMainBinding
import com.example.assignment.room.Database
import com.example.assignment.retrofil.MyNetwork
import com.example.assignment.viewmodel.ViewModeUserFactory
import com.example.assignment.viewmodel.ViewModelUser

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapterUser: AdapterUser
    private var idUserRoom: List<Int> = listOf()
    lateinit var viewModelUser:ViewModelUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapterUser= AdapterUser()
        val viewModeUserFactory=ViewModeUserFactory(Database.getDataBaseUser(this))
        viewModelUser=ViewModelProvider(this,viewModeUserFactory)[ViewModelUser::class.java]
        binding.rclUserList.layoutManager=LinearLayoutManager(this)
        binding.rclUserList.adapter=adapterUser
        viewModelUser.getUser()
        binding.swipeRefresh.setOnRefreshListener {
            binding.rclUserList.visibility=View.INVISIBLE
            getDataUser()
            realUser()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefresh.isRefreshing=false
            },500)
        }
        val myNetwork= MyNetwork(this)
        myNetwork.checkNetWork(object : MyNetwork.Network {
            override fun isNetwork(boolean: Boolean) {
                when(boolean){
                    false-> {
                        realUser()
                    }
                    true->{
                        getDataUser()
                        realUser()
                    }
                }
            }
        })
        clickRecyclerview()
    }
    private fun clickRecyclerview() {
        adapterUser.clickItem={
            val intent=Intent(this, ProfileActivity::class.java)
            intent.putExtra("LOGIN",it.login)
            startActivity(intent)
        }
    }
    private fun getDataUser() {
        viewModelUser.obsUser().observe(this, Observer {listUser->
            if (idUserRoom.isEmpty()){

                listUser?.forEach {
                    viewModelUser.insertUser(it)
                }
            }else{
                listUser?.forEach {user->
                    if (idUserRoom.contains(user.id)){
                        viewModelUser.updaterUser(user)
                    }else {
                        viewModelUser.insertUser(user)
                    }
                }
            }
            adapterUser.setListUser(listUser as MutableList<User>)
        })
    }
    private fun realUser() {
        viewModelUser.obsRealUser().observe(this, Observer {listUser->
            binding.rclUserList.visibility=View.VISIBLE
            adapterUser.setListUser(listUser as MutableList<User>)
            idUserRoom=listUser.map { it.id }
        })
    }
}