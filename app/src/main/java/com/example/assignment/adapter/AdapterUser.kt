package com.example.assignment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.assignment.R
import com.example.assignment.Model.User

class AdapterUser():RecyclerView.Adapter<AdapterUser.ViewHolder>() {
    private var listUser:MutableList<User> = mutableListOf()
    lateinit var clickItem:((User)->Unit)
    fun setListUser(listUser: MutableList<User>){
        this.listUser=listUser
        notifyDataSetChanged()
    }
    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val imgAvatar=itemView.findViewById<ImageView>(R.id.imgAvatar)
        val txtLogin=itemView.findViewById<TextView>(R.id.txtLogin)
        val txtHtml=itemView.findViewById<TextView>(R.id.txtHtml)
        var item: User?=null
        init {
            itemView.setOnClickListener {
                item?.let {user-> clickItem.invoke(user) }
            }
        }
        fun bind(item: User){
            this.item=item
            Glide.with(itemView.context).load(item.avatar_url).into(imgAvatar)
            txtLogin.text=item.login
            txtHtml.text=item.html_url
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUser[position])
    }
}