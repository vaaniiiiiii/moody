package moody.model


data class ApiResponse(
    val status: String,
    val message: String,
    val logs: List<Gambar>
)

data class Gambar(
    val id: String,
    val title: String,
    val mood: String,
    val imageUrl: String
)
