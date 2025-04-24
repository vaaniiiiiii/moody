package moody.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import moody.model.Harian

@Database(entities = [Harian::class], version = 1, exportSchema = false)
abstract class HarianDb : RoomDatabase(){

    abstract val dao: HarianDao

    companion object {

        @Volatile
        private var INSTANCE: HarianDb? = null

        fun getInstance(context: Context): HarianDb {
            synchronized(this){
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HarianDb::class.java,
                        "catatan.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
