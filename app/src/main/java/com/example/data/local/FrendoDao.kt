package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AppSettingsEntity
import com.example.data.model.CommentEntity
import com.example.data.model.FriendEntity
import com.example.data.model.GroupEntity
import com.example.data.model.MessageEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.PostEntity
import com.example.data.model.StoryEntity
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface FrendoDao {

    // === User Profile ===
    @Query("SELECT * FROM users WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(profile: UserProfile)

    @Update
    suspend fun updateUserProfile(profile: UserProfile)

    // === Posts ===
    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE id = :postId LIMIT 1")
    fun getPostById(postId: Long): Flow<PostEntity?>

    @Query("SELECT * FROM posts WHERE isSaved = 1 ORDER BY timestamp DESC")
    fun getSavedPosts(): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity): Long

    @Update
    suspend fun updatePost(post: PostEntity)

    @Query("DELETE FROM posts WHERE id = :postId")
    suspend fun deletePost(postId: Long)

    @Query("UPDATE posts SET isSaved = NOT isSaved WHERE id = :postId")
    suspend fun toggleSavePost(postId: Long)

    @Query("UPDATE posts SET commentsCount = commentsCount + 1 WHERE id = :postId")
    suspend fun incrementPostCommentCount(postId: Long)

    // === Comments ===
    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY timestamp ASC")
    fun getCommentsForPost(postId: Long): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity): Long

    @Query("UPDATE comments SET likeCount = CASE WHEN isLiked = 1 THEN likeCount - 1 ELSE likeCount + 1 END, isLiked = NOT isLiked WHERE id = :commentId")
    suspend fun toggleCommentLike(commentId: Long)

    // === Stories ===
    @Query("SELECT * FROM stories ORDER BY isUserStory DESC, timestamp DESC")
    fun getAllStories(): Flow<List<StoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: StoryEntity): Long

    @Query("UPDATE stories SET isViewed = 1 WHERE id = :storyId")
    suspend fun markStoryViewed(storyId: Long)

    // === Friends ===
    @Query("SELECT * FROM friends ORDER BY name ASC")
    fun getAllFriends(): Flow<List<FriendEntity>>

    @Query("SELECT * FROM friends WHERE status = :status ORDER BY name ASC")
    fun getFriendsByStatus(status: String): Flow<List<FriendEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriends(friends: List<FriendEntity>)

    @Query("UPDATE friends SET status = :newStatus WHERE id = :friendId")
    suspend fun updateFriendStatus(friendId: Long, newStatus: String)

    @Query("DELETE FROM friends WHERE id = :friendId")
    suspend fun deleteFriend(friendId: Long)

    // === Messages ===
    @Query("SELECT * FROM messages WHERE friendId = :friendId ORDER BY timestamp ASC")
    fun getMessagesForFriend(friendId: Long): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    fun getAllMessages(): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity): Long

    // === Notifications ===
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllNotificationsAsRead()

    // === Groups ===
    @Query("SELECT * FROM groups ORDER BY membersCount DESC")
    fun getAllGroups(): Flow<List<GroupEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroups(groups: List<GroupEntity>)

    @Query("UPDATE groups SET isJoined = NOT isJoined, membersCount = CASE WHEN isJoined = 1 THEN membersCount - 1 ELSE membersCount + 1 END WHERE id = :groupId")
    suspend fun toggleGroupMembership(groupId: Long)

    // === Settings ===
    @Query("SELECT * FROM app_settings WHERE id = 1 LIMIT 1")
    fun getSettings(): Flow<AppSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: AppSettingsEntity)

    @Update
    suspend fun updateSettings(settings: AppSettingsEntity)
}
