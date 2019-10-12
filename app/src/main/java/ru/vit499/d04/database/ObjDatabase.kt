package ru.vit499.d04.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Obj::class], version = 3, exportSchema = false)
abstract class ObjDatabase : RoomDatabase() {

    abstract val objDatabaseDao: ObjDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: ObjDatabase? = null

        fun getInstance(context: Context): ObjDatabase {

            var instance = INSTANCE

            if(instance == null){
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