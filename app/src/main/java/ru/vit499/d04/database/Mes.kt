package ru.vit499.d04.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mes_table")
data class Mes (

    @PrimaryKey(autoGenerate = true)
    var mesId : Long = 0L,

    @ColumnInfo(name = "obj_name")
    var numObj : String = "",

    @ColumnInfo(name = "mes_content")
    var mesContent : String = "",

    @ColumnInfo(name = "mes_time1")
    var mesTime1 : String = "",

    @ColumnInfo(name = "mes_time2")
    var mesTime2 : String = ""
)