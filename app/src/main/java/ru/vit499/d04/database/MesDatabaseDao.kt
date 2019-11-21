package ru.vit499.d04.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MesDatabaseDao {

    @Insert
    fun insert(mes: Mes)

    @Query("SELECT * from mes_table WHERE obj_name = :obj_name ORDER BY mesId DESC")
    fun getMesByName(obj_name : String) : List<Mes>?

    @Query("DELETE from mes_table WHERE obj_name = :obj_name")
    fun deleteMesObj(obj_name : String)
}