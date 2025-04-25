package moody.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import moody.model.Harian

@Dao
interface HarianDao {

    @Insert
    suspend fun insert(harian: Harian)

    @Update
    suspend fun update(harian: Harian)

    @Query("SELECT * FROM harian ORDER BY tanggal DESC")
    fun getHarian(): Flow<List<Harian>>

    @Query ("SELECT * FROM harian WHERE id = :id")
    suspend fun getHarianById(id: Long): Harian?

    @Query("DELETE FROM harian WHERE id = :id")
    suspend fun deleteById(id: Long)

}