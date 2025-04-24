package moody.model

data class Harian(
    val id: Long,
    val judul: String,
    val catatan: String,
    val mood: String,
    val tanggal: String
)
