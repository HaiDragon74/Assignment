package com.example.assignment.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.assignment.Model.ProfileUser
import com.example.assignment.Model.User

@androidx.room.Database(entities =[User::class,ProfileUser::class], version = 1, exportSchema = false)
abstract class Database():RoomDatabase() {
    abstract fun dao():Dao
    abstract fun daoProfile():DaoProfile
    companion object{
        @Volatile
        private var INSTANCE:Database?=null
        fun getDataBaseUser(context:Context):Database{
            val instance= INSTANCE
            if (instance!=null){
                return instance
            }
            synchronized(this){
                val newInstance=Room.databaseBuilder(
                    context.applicationContext,
                    Database::class.java,
                    "tableDatabase"
                ).build()
                INSTANCE=newInstance
                return newInstance
            }
        }
    }
}