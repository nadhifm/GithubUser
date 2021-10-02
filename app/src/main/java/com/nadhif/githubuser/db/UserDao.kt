package com.nadhif.githubuser.db

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.nadhif.githubuser.network.response.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM users")
    fun getAllFavoriteUsers(): LiveData<List<User>>

    @Query("SELECT * FROM users")
    fun getListFavoriteUsers(): List<User>

    @Query("SELECT * FROM users WHERE login = :username")
    fun isFavorite(username: String): LiveData<List<User>>

    @Query("SELECT * FROM users")
    fun cursorSelectAll(): Cursor

    @Query("SELECT * FROM users WHERE id = :id")
    fun cursorSelectById(id: Long): Cursor

    @Query("DELETE FROM users WHERE login IN (:selectedUser)")
    suspend fun deleteSelectedUser(selectedUser: ArrayList<String>)

    @Delete
    suspend fun deleteUser(user: User)
}