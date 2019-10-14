package ru.vit499.d04.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ObjDatabaseDao {

    @Insert
    fun insert(obj: Obj)

    @Update
    fun update(obj: Obj)

    @Query("SELECT * from obj_table WHERE objId = :key")
    fun getObjById(key: Long): Obj?

    @Query("DELETE FROM obj_table")
    fun deleteAllObj()

    @Query("SELECT * from obj_table ORDER BY objId DESC")
    fun getAllObj1(): LiveData<List<Obj>>

    @Query("SELECT * from obj_table ORDER BY objId DESC")
    fun getAllObj(): List<Obj>

    @Query("SELECT * from obj_table WHERE obj_name = :obj_name")
    fun getObjByName(obj_name: String): Obj?

    @Query("DELETE FROM obj_table WHERE obj_name = :obj_name")
    fun deleteObjByName(obj_name: String)

    @Query("SELECT * FROM obj_table ORDER BY objId DESC LIMIT 1")
    fun getObj(): Obj?

}