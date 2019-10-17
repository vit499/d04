package ru.vit499.d04.objstate

class ObjDb {

    companion object{

        val COLUMN_ID = "id"
        val COLUMN_NUM = "num"
        val COLUMN_DESCR = "event"
        val COLUMN_CODE = "time"

        val COLUMN_CFG_PART1 = "ZoneInPart1"
        val COLUMN_CFG_PART2 = "ZoneInPart2"
        val COLUMN_CFG_PART3 = "ZoneInPart3"
        val COLUMN_CFG_PART4 = "ZoneInPart4"
        val COLUMN_CFG_PART5 = "ZoneInPart5"
        val COLUMN_CFG_PART6 = "ZoneInPart6"
        val COLUMN_CFG_PART7 = "ZoneInPart7"
        val COLUMN_CFG_PART8 = "ZoneInPart8"
        val COLUMN_CFG_PART9 = "ZoneInPart9"
        val COLUMN_CFG_PART10 = "ZoneInPart10"
        val COLUMN_CFG_PART11 = "ZoneInPart11"
        val COLUMN_CFG_PART12 = "ZoneInPart12"
        val COLUMN_CFG_PART13 = "ZoneInPart13"
        val COLUMN_CFG_PART14 = "ZoneInPart14"
        val COLUMN_CFG_PART15 = "ZoneInPart15"
        val COLUMN_CFG_PART16 = "ZoneInPart16"

        val COLUMN_ZONESTAT = "ZoneStat"    // 18
        val COLUMN_ZONEALARM = "ZoneAlarm"
        val COLUMN_PARTSTAT = "PartStat"
        val COLUMN_FOUT = "fout"
        val COLUMN_FTOUT = "ftout"
        val COLUMN_SOUT = "sout"
        val COLUMN_TRBL = "trbl"

        val COLUMN_TEMP1 = "dv_t1"    // 25
        val COLUMN_TEMP2 = "dv_t2"
        val COLUMN_TEMP3 = "dv_t3"
        val COLUMN_TEMP4 = "dv_t4"
        val COLUMN_TEMP5 = "dv_t5"
        val COLUMN_TEMP6 = "dv_t6"
        val COLUMN_TEMP7 = "dv_t7"
        val COLUMN_TEMP8 = "dv_t8"
        val COLUMN_TEMP9 = "dv_t9"
        val COLUMN_TEMP10 = "dv_t10"
        val COLUMN_TEMP11 = "dv_t11"
        val COLUMN_TEMP12 = "dv_t12"
        val COLUMN_U12 = "dv_12v"    // 37
        val COLUMN_U3 = "dv_3v"
        val COLUMN_GSM1 = "dv_gsm1"   // 39
        val COLUMN_GSM2 = "dv_gsm2"
        val COLUMN_TCP = "tcp"       // 41
        val COLUMN_VERS = "vers"

        val COLUMN_EVENT = "dv_event"
        val COLUMN_TIME = "dv_time"

        val COLUMN_RES1 = "reserv1"    // 25
        val COLUMN_RES2 = "reserv2"
        val COLUMN_RES3 = "reserv3"
        val COLUMN_RES4 = "reserv4"
        val COLUMN_RES5 = "reserv5"
        val COLUMN_RES6 = "reserv6"
        val COLUMN_RES7 = "reserv7"
        val COLUMN_RES8 = "reserv8"
        val COLUMN_RES9 = "reserv9"
        val COLUMN_RES10 = "reserv10"
        val COLUMN_RES11 = "reserv11"
        val COLUMN_RES12 = "reserv12"
        val COLUMN_RES13 = "reserv13"
        val COLUMN_RES14 = "reserv14"
        val COLUMN_RES15 = "reserv15"
        val COLUMN_RES16 = "reserv16"
        val COLUMN_TIME_LOCAL = "timelocal"

        internal var dbStr = arrayOf<String>(
            COLUMN_ID, // 0
            COLUMN_NUM,
            COLUMN_DESCR,
            COLUMN_CODE,

            COLUMN_CFG_PART1, // 4
            COLUMN_CFG_PART2, // 5
            COLUMN_CFG_PART3,
            COLUMN_CFG_PART4,
            COLUMN_CFG_PART5,
            COLUMN_CFG_PART6,
            COLUMN_CFG_PART7,
            COLUMN_CFG_PART8,
            COLUMN_CFG_PART9,
            COLUMN_CFG_PART10,
            COLUMN_CFG_PART11,
            COLUMN_CFG_PART12,
            COLUMN_CFG_PART13,
            COLUMN_CFG_PART14,
            COLUMN_CFG_PART15,
            COLUMN_CFG_PART16,

            COLUMN_ZONESTAT, // 20
            COLUMN_ZONEALARM,
            COLUMN_PARTSTAT,
            COLUMN_FOUT, // 23
            COLUMN_FTOUT,
            COLUMN_SOUT,
            COLUMN_TRBL, // 26

            COLUMN_TEMP1, // 27
            COLUMN_TEMP2,
            COLUMN_TEMP3,
            COLUMN_TEMP4,
            COLUMN_TEMP5,
            COLUMN_TEMP6,
            COLUMN_TEMP7,
            COLUMN_TEMP8,
            COLUMN_TEMP9,
            COLUMN_TEMP10,
            COLUMN_TEMP11,
            COLUMN_TEMP12,

            COLUMN_U12, // 39
            COLUMN_U3, // 40
            COLUMN_GSM1, // 41
            COLUMN_GSM2,
            COLUMN_TCP, // 43
            COLUMN_VERS, // 44
            COLUMN_EVENT, // 45
            COLUMN_TIME, // 46
            COLUMN_RES1, // 47
            COLUMN_RES2,
            COLUMN_RES3,
            COLUMN_RES4,
            COLUMN_RES5,
            COLUMN_RES6,
            COLUMN_RES7,
            COLUMN_RES8,
            COLUMN_RES9,
            COLUMN_RES10,
            COLUMN_RES11,
            COLUMN_RES12,
            COLUMN_RES13,
            COLUMN_RES14,
            COLUMN_RES15,
            COLUMN_RES16,
            COLUMN_TIME_LOCAL      // 63
        )

        fun getStrDbColumns(): Array<String> {
            return dbStr
        }
        fun getColumnCount(): Int {
            return dbStr.size
        }
    }
}