package com.example.textify.DAOs

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.textify.models.User

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Query("SELECT * FROM users WHERE id = :uid")
    suspend fun getUser(uid: String): User?

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    @Query("UPDATE users SET name = :username WHERE id = :uid")
    suspend fun updateUsername(uid: String, username: String)

    @Query("UPDATE users SET online_status = :status WHERE id = :uid")
    suspend fun updateOnlineStatus(uid: String, status: Boolean)

    @Query("UPDATE users SET image = :image WHERE id = :uid")
    suspend fun updateImage(uid: String, image: String)

    @Query("UPDATE users SET status = :status WHERE id = :uid")
    suspend fun updateStatus(uid: String, status: String)
}