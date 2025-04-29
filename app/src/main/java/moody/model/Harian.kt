package moody.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "harian")
data class Harian(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val judul: String,
    val harian: String,
    val mood: String,
    val tanggal: String,
    val isDeleted: Boolean = false
)
