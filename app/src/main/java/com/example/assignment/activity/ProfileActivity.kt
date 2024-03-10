package com.example.assignment.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.assignment.Model.ProfileUser
import com.example.assignment.databinding.ActivityProfileBinding
import com.example.assignment.room.Database
import com.example.assignment.retrofil.MyNetwork
import com.example.assignment.viewmodel.ViewModeProfileUserlFactory
import com.example.assignment.viewmodel.ViewModelProfileUser

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModelProfileUser: ViewModelProfileUser
    private var idUserRoom:Int?=null
    private var login:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModeProfileUserFactory=ViewModeProfileUserlFactory(Database.getDataBaseUser(this))
        viewModelProfileUser=ViewModelProvider(this,viewModeProfileUserFactory)[ViewModelProfileUser::class.java]
        val intent=intent
        login= intent.getStringExtra("LOGIN")
        viewModelProfileUser.getProfileUser(login!!)
        binding.refreshLayoutProfile.setOnRefreshListener {
            binding.layoutProfile.visibility=View.INVISIBLE
            viewModelProfileUser.getProfileUser(login!!)
            realProfileUser()
            getDataProfileUser()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.refreshLayoutProfile.isRefreshing=false
            },500)
        }
        val myNetwork= MyNetwork(this)
        myNetwork.checkNetWork(object : MyNetwork.Network {
            override fun isNetwork(boolean: Boolean) {
                when(boolean){
                    false-> { realProfileUser() }
                    true->{
                        getDataProfileUser()
                        realProfileUser()
                    }
                }
            }
        })
    }
    private fun getDataProfileUser() {
        viewModelProfileUser.obsProfileUser().observe(this, Observer { profileUser->
            binding.layoutProfile.visibility=View.VISIBLE
            if (idUserRoom==null){
                viewModelProfileUser.insertProfile(profileUser)
            }else if (idUserRoom==profileUser.id){
                viewModelProfileUser.updaterProfile(profileUser)
            }else{
                viewModelProfileUser.insertProfile(profileUser)
            }
            loadData(profileUser)
        })
    }
    private fun realProfileUser() {
        viewModelProfileUser.realProfile().observe(this, Observer {listProfileUser->
            listProfileUser.forEach {profileUser->
                idUserRoom=profileUser.id
                if (login==profileUser.login){
                    loadData(profileUser)
                }
            }
        })
    }
    private fun loadData(profileUser:ProfileUser) {
        Glide.with(this).load(profileUser.avatar_url).into(binding.imgAvatar)
        binding.txtUserName.text=profileUser.name
        binding.txtLocation.text=profileUser.location
        binding.txtBio.text=profileUser.bio.toString()
        if (binding.txtBio.text=="null"){
            binding.txtBio.visibility=View.INVISIBLE
        }else
            binding.txtBio.visibility=View.VISIBLE
        binding.txtNumberPublicRepo.text=profileUser.public_repos.toString()
        binding.txtNumberFollowers.text=profileUser.followers.toString()
        binding.txtNumberFollowing.text=profileUser.following.toString()
        binding.layoutProfile.visibility=View.VISIBLE
    }
}