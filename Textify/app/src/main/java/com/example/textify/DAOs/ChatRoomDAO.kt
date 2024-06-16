package com.example.textify.DAOs

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.textify.models.ChatRoom

@Dao
interface ChatRoomDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatRoom(chatRoom: ChatRoom)

    @Query("SELECT * FROM chat_rooms WHERE id = :chatRoomId")
    suspend fun getChatRoomById(chatRoomId: String): ChatRoom?

    @Query("SELECT * FROM chat_rooms")
    suspend fun getAllChatRooms(): List<ChatRoom>
}