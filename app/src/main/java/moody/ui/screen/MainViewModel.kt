package screen

import androidx.lifecycle.ViewModel
import moody.model.Harian

class MainViewModel : ViewModel() {
    val data = listOf(
        Harian(
            1,
            "kuliah mobpro 17 feb",
            "kuliah hari ini rada cape tapi juga lumayan seru!",
            "2025-02-17 12:34:56",
            ""
        ),
        Harian(
            2,
            "kuliah pag 18 feb",
            "PAG bikin game laptopku rada lag ya :D",
            "2025-02-18 16:45:34",
            ""
        ),

        Harian(
            10,
            "pulkam 28 mar",
            "akhirnya pulkam, ga sabar hari raya. minal aidzin wal faidzin semua!",
            "2025-03-28 05:43:09",
            ""
        )
    )
    fun getHarian(id: Long): Harian? {
        return data.find { it.id == id }
    }
}