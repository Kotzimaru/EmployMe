package ru.practicum.android.diploma.util.storage.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import ru.practicum.android.diploma.util.storage.db.entity.FavoriteEntity

@Dao
interface FavoriteDao {

    @Insert(entity = FavoriteEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorite_table WHERE id LIKE :id")
    suspend fun deleteFavorite(id: String)

    @Query("SELECT * FROM favorite_table")
    suspend fun getFavorites(): List<FavoriteEntity>

    @Query("SELECT * FROM favorite_table WHERE id LIKE :id")
    suspend fun getFavorite(id: String): List<FavoriteEntity>
}
