package com.example.assignment.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.assignment.Model.ProfileUser
import com.example.assignment.Model.User

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)
    @Query("SELECT * FROM tableUser")
    fun realUser():LiveData<List<User>>
    @Update
    suspend fun updaterUser(user: User)
}
@Dao
interface DaoProfile{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profileUser: ProfileUser)
    @Query("SELECT * FROM tableProfileUser")
    fun realProfile():LiveData<List<ProfileUser>>
    @Update
    suspend fun updaterProfile(profileUser: ProfileUser)
    @Delete
    fun deleteProfile(profileUser: ProfileUser)
}
