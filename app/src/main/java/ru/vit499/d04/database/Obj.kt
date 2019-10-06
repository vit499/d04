package ru.vit499.d04.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "obj_table")
data class Obj(

    @PrimaryKey(autoGenerate = true)
    var objId: Long = 0L,

    @ColumnInfo(name = "obj_name")
    val objName: String = "",

    @ColumnInfo(name = "obj_descr")
    val objDescr: String = "",

    @ColumnInfo(name = "obj_code")
    val objCode: String = ""
)