package ru.vit499.d04.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.vit499.d04.util.Logm

@Database(entities = [Mes::class], version = 1, exportSchema = false)
abstract class MesDatabase : RoomDatabase() {

    abstract val mesDatabaseDao : MesDatabaseDao

    companion object{

        @Volatile
        private var INSTANCE : MesDatabase? = null

        fun getInstance(context : Context) : MesDatabase {
            var instance = INSTANCE
            if(instance == null) {
                Logm.aa("create instance mes ---- ")
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MesDatabase::class.java,
                    "mes_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
            }
            return instance
        }
    }
}