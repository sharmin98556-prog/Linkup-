package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.AppSettingsEntity
import com.example.data.model.CommentEntity
import com.example.data.model.FriendEntity
import com.example.data.model.GroupEntity
import com.example.data.model.MessageEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.PostEntity
import com.example.data.model.StoryEntity
import com.example.data.model.UserProfile

@Database(
    entities = [
        UserProfile::class,
        PostEntity::class,
        CommentEntity::class,
        StoryEntity::class,
        FriendEntity::class,
        MessageEntity::class,
        NotificationEntity::class,
        GroupEntity::class,
        AppSettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FrendoDatabase : RoomDatabase() {
    abstract fun frendoDao(): FrendoDao

    companion object {
        @Volatile
        private var INSTANCE: FrendoDatabase? = null

        fun getDatabase(context: Context): FrendoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FrendoDatabase::class.java,
                    "frendo_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
