package screen

import androidx.lifecycle.ViewModel
import moody.model.Harian

class MainViewModel : ViewModel() {
    val data = listOf(
        Harian(
            1,
            "kuliah mobpro 17 feb",
            "kuliah hari ini rada cape tapi juga lumayan seru!",
            "2025-02-17 12:34:56"
        ),
        Harian(
            2,
            "kuliah pag 18 feb",
            "PAG bikin game laptopku rada lag ya :D",
            "2025-02-18 16:45:34"
        ),
        Harian(
            3,
            "kuliah penat 01 mar",
            "penatpun harus tetap semangat meluluskan diri sendiri",
            "2025-03-01 17:54:03"
        ),
        Harian(
            4,
            "kangen kucing rumah 04 mar",
            "kucingku di rumah ada banyak banget, kangen semuanya",
            "2025-03-04 19:24:01"
        ),
        Harian(
            5,
            "salah jurusan? 15 mar",
            "coding, coding, coding. itu aja isi iT sanggp ga sanggup harus selesai.",
            "2025-03-15 10:16:28"
        ),
        Harian(
            6,
            "kuliah kwu 19 mar",
            "kwu isinya duit terus, namanya juga disuruh buka usaha jualan",
            "2025-03-19 16:30:59"
        ),
        Harian(
            7,
            "stand kwu 20 mar",
            "seru banget si berasa jualan beneran kaya orang-orang",
            "2025-03-20 07:00:23"
        ),
        Harian(
            8,
            "bikin tugas imersif 22 mar",
            "ternyata seru banget imersif, apalahi mindARnya.",
            "2025-03-22 15:09:32"
        ),
        Harian(
            9,
            "PT tubes 26 mar",
            "codenya pake framework laravel, udah exicted banget ngodingnya" +
                    "taunya awal-awal pun udah error",
            "2025-03-26 12:39:51"
        ),
        Harian(
            10,
            "pulkam 28 mar",
            "akhirnya pulkam, ga sabar hari raya. minal aidzin wal faidzin semua!",
            "2025-03-28 05:43:09"
        )

    )
}