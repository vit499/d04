package ru.vit499.d04.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "obj_table")
data class Obj(

    @PrimaryKey(autoGenerate = true)
    var objId: Long = 0L,

    @ColumnInfo(name = "obj_name")
    var objName: String = "",

    @ColumnInfo(name = "obj_descr")
    var objDescr: String = "",

    @ColumnInfo(name = "obj_code")
    var objCode: String = "",

    //------------------------------------ zoneInPart
    @ColumnInfo(name = "zone_in_part1")
    var ZoneInPart1: String = "",

    @ColumnInfo(name = "zone_in_part2")
    var ZoneInPart2: String = "",

    @ColumnInfo(name = "zone_in_part3")
    var ZoneInPart3: String = "",

    @ColumnInfo(name = "zone_in_part4")
    var ZoneInPart4: String = "",

    @ColumnInfo(name = "zone_in_part5")
    var ZoneInPart5: String = "",

    @ColumnInfo(name = "zone_in_part6")
    var ZoneInPart6: String = "",

    @ColumnInfo(name = "zone_in_part7")
    var ZoneInPart7: String = "",

    @ColumnInfo(name = "zone_in_part8")
    var ZoneInPart8: String = "",

    @ColumnInfo(name = "zone_in_part9")
    var ZoneInPart9: String = "",

    @ColumnInfo(name = "zone_in_part10")
    var ZoneInPart10: String = "",

    @ColumnInfo(name = "zone_in_part11")
    var ZoneInPart11: String = "",

    @ColumnInfo(name = "zone_in_part12")
    var ZoneInPart12: String = "",

    @ColumnInfo(name = "zone_in_part13")
    var ZoneInPart13: String = "",

    @ColumnInfo(name = "zone_in_part14")
    var ZoneInPart14: String = "",

    @ColumnInfo(name = "zone_in_part15")
    var ZoneInPart15: String = "",

    @ColumnInfo(name = "zone_in_part16")
    var ZoneInPart16: String = "",

    //--------------------------------- zoneStat ---
    @ColumnInfo(name = "zone_stat")
    var ZoneStat: String = "",

    @ColumnInfo(name = "zone_alarm")
    var ZoneAlarm: String = "",

    @ColumnInfo(name = "part_stat")
    var PartStat: String = "",

    @ColumnInfo(name = "fout")
    var fout: String = "",

    @ColumnInfo(name = "ftout")
    var ftout: String = "",

    @ColumnInfo(name = "sout")
    var sout: String = "",

    @ColumnInfo(name = "trbl")
    var trbl: String = "",

    //---------------- temper ------------------
    @ColumnInfo(name = "dv_t0")
    var temp0: String = "",

    @ColumnInfo(name = "dv_t1")
    var temp1: String = "",

    @ColumnInfo(name = "dv_t2")
    var temp2: String = "",

    @ColumnInfo(name = "dv_t3")
    var temp3: String = "",

    @ColumnInfo(name = "dv_t4")
    var temp4: String = "",

    @ColumnInfo(name = "dv_t5")
    var temp5: String = "",

    @ColumnInfo(name = "dv_t6")
    var temp6: String = "",

    @ColumnInfo(name = "dv_t7")
    var temp7: String = "",

    @ColumnInfo(name = "dv_t8")
    var temp8: String = "",

    @ColumnInfo(name = "dv_t9")
    var temp9: String = "",

    @ColumnInfo(name = "dv_t10")
    var temp10: String = "",

    @ColumnInfo(name = "dv_t11")
    var temp11: String = "",

    @ColumnInfo(name = "dv_t12")
    var temp12: String = "",

    //------------------------------ 12v ---
    @ColumnInfo(name = "dv_12v")
    var dv12v: String = "",

    @ColumnInfo(name = "dv_3v")
    var dv3v: String = "",

    @ColumnInfo(name = "dv_gsm1")
    var gsm1: String = "",

    @ColumnInfo(name = "dv_gsm2")
    var gsm2: String = "",

    @ColumnInfo(name = "tcp")
    var tcp: String = "",

    @ColumnInfo(name = "vers")
    var vers: String = "",

    //------------------------------- event --
    @ColumnInfo(name = "dv_event")
    var objEvent: String = "",

    @ColumnInfo(name = "dv_time")
    var objTime: String = "",

    //----------------------------- reserv --
    @ColumnInfo(name = "reserv1")
    var reserv1: String = "",

    @ColumnInfo(name = "reserv2")
    var reserv2: String = "",

    @ColumnInfo(name = "reserv3")
    var reserv3: String = "",

    @ColumnInfo(name = "reserv4")
    var reserv4: String = "",

    @ColumnInfo(name = "reserv5")
    var reserv5: String = "",

    @ColumnInfo(name = "reserv6")
    var reserv6: String = "",

    @ColumnInfo(name = "reserv7")
    var reserv7: String = "",

    @ColumnInfo(name = "reserv8")
    var reserv8: String = "",

    @ColumnInfo(name = "reserv9")
    var reserv9: String = "",

    @ColumnInfo(name = "reserv10")
    var reserv10: String = "",

    @ColumnInfo(name = "reserv11")
    var reserv11: String = "",

    @ColumnInfo(name = "reserv12")
    var reserv12: String = "",

    @ColumnInfo(name = "reserv13")
    var reserv13: String = "",

    @ColumnInfo(name = "reserv14")
    var reserv14: String = "",

    @ColumnInfo(name = "reserv15")
    var reserv15: String = "",

    @ColumnInfo(name = "reserv16")
    var reserv16: String = "",

    @ColumnInfo(name = "time_local")
    var timeLocal: String = ""
)