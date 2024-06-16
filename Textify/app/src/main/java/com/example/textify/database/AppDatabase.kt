package com.example.textify.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.textify.DAOs.ChatRoomDAO
import com.example.textify.DAOs.UserDAO
import com.example.textify.models.ChatRoom
import com.example.textify.models.User
import com.example.textify.utils.Converters

@Database(entities = [User::class, ChatRoom::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
    abstract fun chatRoomDao(): ChatRoomDAO
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addMigrations(migration_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
        private val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `chat_rooms` " +
                        "(`id` TEXT NOT NULL, " +
                        "`receiver_id` TEXT NOT NULL, " +
                        "`receiver_name` TEXT NOT NULL, " +
                        "`receiver_image` TEXT NOT NULL, " +
                        "`receiver_activity` TEXT NOT NULL, " +
                        "`receiver_thoughts` TEXT NOT NULL, " +
                        "`last_text` TEXT NOT NULL, " +
                        "`last_text_from` TEXT NOT NULL, " +
                        "`last_msg_id` TEXT NOT NULL, " +
                        "`timestamp` INTEGER NOT NULL, " +
                        "`date_added` INTEGER NOT NULL, " +
                        "`message_number` INTEGER NOT NULL, " +
                        "`sender_id` TEXT NOT NULL, " +
                        "`sender_name` TEXT NOT NULL, " +
                        "`sender_image` TEXT NOT NULL, " +
                        "`sender_activity` TEXT NOT NULL, " +
                        "`sender_thoughts` TEXT NOT NULL, " +
                        "`unread_count` INTEGER NOT NULL, " +
                        "PRIMARY KEY(`id`))")
            }
        }
    }
}