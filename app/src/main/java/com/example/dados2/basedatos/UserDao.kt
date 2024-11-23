package com.example.dados2.basedatos

import androidx.room.*
import com.example.dados2.hub.User

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAllUsers(): List<User>

    @Query("SELECT * FROM users WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM users WHERE first_name LIKE :firstName")
    fun findByName(firstName: String): User

    @Query("SELECT COUNT(*) FROM users WHERE first_name LIKE :firstName")
    fun countByName(firstName: String): Int

    @Insert
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("DELETE FROM users")
    fun deleteAllUsers()
}