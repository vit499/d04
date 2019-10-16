package ru.vit499.d04.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.vit499.d04.util.Logm

@Database(entities = [Obj::class], version = 3, exportSchema = false)
abstract class ObjDatabase : RoomDatabase() {

    abstract val objDatabaseDao: ObjDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: ObjDatabase? = null

        fun getInstance(context: Context): ObjDatabase {

            var instance = INSTANCE

            if(instance == null){
                Log.i("aa", "create instance --------------- ")
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    ObjDatabase::class.java,
                "obj_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}