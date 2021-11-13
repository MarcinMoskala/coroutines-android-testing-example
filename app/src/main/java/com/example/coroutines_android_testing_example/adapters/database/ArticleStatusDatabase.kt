package com.example.coroutines_android_testing_example.adapters.database

import android.content.Context
import androidx.room.*
import com.example.coroutines_android_testing_example.domain.ArticleStatus
import com.example.coroutines_android_testing_example.domain.ArticleStatusRepository

class ArticleStatusDatabase(
    applicationContext: Context
) : ArticleStatusRepository {
    private val db = Room
        .databaseBuilder(applicationContext, AppDatabase::class.java, "database-name")
        .build()
    private val statusesDao = db.articleStatusDao()

    override suspend fun getArticleStatuses(): List<ArticleStatus> =
        statusesDao.getAll()
            .map { it.toArticleStatus() }

    override suspend fun addNewArticleKeys(articleKeys: List<String>) =
        statusesDao.insertAll(articleKeys.map {
            ArticleStatusEntity(
                articleKey = it,
                status = ArticleStatusEntity.Status.UNSEEN
            )
        })

    override suspend fun changeArticleStatus(articleStatus: ArticleStatus) {
        statusesDao.upsert(articleStatus.toArticleStatusEntity())
    }
}

fun ArticleStatusEntity.toArticleStatus() = ArticleStatus(
    articleKey = articleKey,
    articleStatus = when (status) {
        ArticleStatusEntity.Status.UNSEEN -> ArticleStatus.Status.UNSEEN
        ArticleStatusEntity.Status.SEEN -> ArticleStatus.Status.SEEN
        ArticleStatusEntity.Status.READ -> ArticleStatus.Status.READ
    }
)

fun ArticleStatus.toArticleStatusEntity() = ArticleStatusEntity(
    articleKey = articleKey,
    status = when (articleStatus) {
        ArticleStatus.Status.UNSEEN -> ArticleStatusEntity.Status.UNSEEN
        ArticleStatus.Status.SEEN -> ArticleStatusEntity.Status.SEEN
        ArticleStatus.Status.READ -> ArticleStatusEntity.Status.READ
    }
)

@Entity
data class ArticleStatusEntity(
    @PrimaryKey val uid: Int? = null,
    @ColumnInfo(name = "articleKey") val articleKey: String,
    @ColumnInfo(name = "status") val status: Status
) {
    enum class Status {
        UNSEEN, SEEN, READ
    }
}

@Dao
interface ArticleStatusDao {
    @Query("SELECT * FROM articlestatusentity")
    suspend fun getAll(): List<ArticleStatusEntity>

    @Insert
    fun insertAll(users: List<ArticleStatusEntity>)

    @Delete
    fun delete(user: ArticleStatusEntity)

    @Insert
    fun insert(entity: ArticleStatusEntity): Long

    @Update
    fun update(entity: ArticleStatusEntity)

    @Transaction
    fun upsert(entity: ArticleStatusEntity) {
        val id = insert(entity)
        if (id == -1L) {
            update(entity)
        }
    }
}

@Database(entities = [ArticleStatusEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleStatusDao(): ArticleStatusDao
}