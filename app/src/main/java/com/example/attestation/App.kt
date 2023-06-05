package com.example.attestation

import android.app.Application
import androidx.paging.PagingSource
import androidx.room.*

class App : Application() {
    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "db"
        ).build()
    }
}

@Database(
    entities = [
        FeedPhoto::class,
        RemoteKeys::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getFeedPhotoDao(): FeedPhotoDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao
}

@Entity(tableName = "FeedPhoto")
data class FeedPhoto(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String, //ИД
    @ColumnInfo(name = "urlsSmall")
    val urlsSmall: String, //Маленькая картинка ленты
    @ColumnInfo(name = "likes")
    val likes: Int, //Количество лайков
    @ColumnInfo(name = "liked_by_user")
    val liked_by_user: Boolean, //ичный лайк
    @ColumnInfo(name = "nameAuthor")
    val nameAuthor: String, //Имя автора
    @ColumnInfo(name = "usernameAuthor")
    val usernameAuthor: String, //Ник автора
    @ColumnInfo(name = "userAvatar")
    val userAvatar: String //Аватарка автора
)

@Dao
interface FeedPhotoDao {
    @Query("SELECT*FROM FeedPhoto")
    fun getAll(): PagingSource<Int, FeedPhoto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: List<FeedPhoto>)

    @Query("Delete From FeedPhoto")
    suspend fun delete()

    @Update
    suspend fun update(data: FeedPhoto)
}

@Entity(tableName = "remote_key")
data class RemoteKeys(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "photo_id")
    val photoId: String,
    val prevKey: Int?,
    val currentPage: Int,
    val nextKey: Int?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("Select * From remote_key Where photo_id = :id")
    suspend fun getRemoteKeyByPhotoID(id: String): RemoteKeys?

    @Query("Delete From remote_key")
    suspend fun clearRemoteKeys()

    @Query("Select created_at From remote_key Order By created_at DESC LIMIT 1")
    suspend fun getCreationTime(): Long?
}