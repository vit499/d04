package ru.vit499.d04.objstate

import ru.vit499.d04.database.Obj
import ru.vit499.d04.ui.main.StatList
import ru.vit499.d04.util.Buf
import ru.vit499.d04.util.Logm
import ru.vit499.d04.util.Str

class ObjState(obj: Obj) {

    val NUMBER_ZONE = 64
    val NUMBER_PART = 16
    val NUMBER_OUT = 32
    val NUMBER_TEMP = 12

    internal var id: Int = 0
    internal var num: String = ""
    internal var descr: String = ""
    internal var code: String = ""

    internal var configPart = arrayOfNulls<String>(NUMBER_PART)
    internal var strTemp = arrayOfNulls<String>(NUMBER_TEMP)
    internal var strGsm = arrayOfNulls<String>(2)

    internal var zoneStat = IntArray(NUMBER_ZONE)
    internal var partStat = IntArray(NUMBER_PART)
    internal var zoneAlarm = IntArray(NUMBER_ZONE)
    internal var functOut = IntArray(NUMBER_OUT)
    internal var statOut = IntArray(NUMBER_OUT)
    internal var zonePresentInPart = Array(NUMBER_PART) { IntArray(NUMBER_ZONE) }
    internal var partPresent = IntArray(NUMBER_PART)

    //
    internal var strZoneStat: String = ""
    internal var strZoneAlarm: String = ""
    internal var strPartStat: String = ""
    internal var strFout: String = ""
    internal var strFtout: String = ""
    internal var strSout: String = ""
    internal var strTrbl: String = ""
    internal var strEvent: String = ""
    internal var str12V: String = ""
    internal var str3V: String = ""
    internal var strTcp: String = ""
    internal var strVers: String = ""
    internal var timeLastData: String = ""
    internal var strTime: String = ""


    init {
        fillDevDataByte(obj)
    }

    fun fillEmpty() {
        for (i in 0 until NUMBER_PART) {
            configPart[i] = "0000000000000000"
        }
        strZoneStat = "0000000000000000"
        strZoneAlarm = "0000000000000000"
        strPartStat = "00000000000000000000000000000000"
        strFout = "0000000000000000000000000000000000000000000000000000000000000000"
        strFtout = "0000000000000000000000000000000000000000000000000000000000000000"
        strSout = "00000000"
        strTrbl = "000000"
        for (i in strTemp.indices) {
            strTemp[i] = "n"
        }
        str12V = "0"
        str3V = "0"
        for (i in strGsm.indices) {
            strGsm[i] = "0"
        }
        strTcp = "000000"
        strVers = "0"
        strEvent = "0"
        strTime = "0"

        var timeLastData = ""
        // fill
        for (i in 0 until NUMBER_ZONE) {
            zoneStat[i] = 0
            zoneAlarm[i] = 0
        }
        for (i in 0 until NUMBER_OUT) {
            functOut[i] = 0
            statOut[i] = 0
        }
        for (i in 0 until NUMBER_PART) {
            partStat[i] = 1
            for (j in 0 until NUMBER_ZONE) zonePresentInPart[i][j] = 0
            partPresent[i] = 0
        }
    }


    fun fillStrings(obj : Obj, s: Array<String>) {
//        var ind = 4
//        for (i in 0 until NUMBER_PART) {
//            configPart[i] = s[ind++]  // 4,5...
//        }
//        strZoneStat = s[ind++]
//        strZoneAlarm = s[ind++]
//        strPartStat = s[ind++]
//        strFout = s[ind++]
//        strFtout = s[ind++]
//        strSout = s[ind++]  // 25
//        strTrbl = s[ind++]  //
//
//        for (i in strTemp.indices) {
//            strTemp[i] = s[ind++]
//        }
//        str12V = s[ind++]             // 39
//        str3V = s[ind++]
//
//        for (i in strGsm.indices) {
//            strGsm[i] = s[ind++]
//        }
//        strTcp = s[ind++]
//        strVers = s[ind++]
//        strEvent = s[ind++]
//        strTime = s[ind++]

        fillDevDataByte(obj)
    }

    fun getAllStr(): Array<String> {
        val k = ObjDb.getColumnCount()
        val s = arrayOfNulls<String>(k)
        var ind = 0
        s[ind++] = "0"
        s[ind++] = num
        s[ind++] = descr
        s[ind++] = code
        for (i in 0 until NUMBER_PART) {
            s[ind++] = configPart[i]
        }
        s[ind++] = strZoneStat
        s[ind++] = strZoneAlarm
        s[ind++] = strPartStat
        s[ind++] = strFout
        s[ind++] = strFtout
        s[ind++] = strSout  // 25
        s[ind++] = strTrbl  //

        for (i in 0 until NUMBER_TEMP) {
            s[ind++] = strTemp[i]
        }
        s[ind++] = str12V             // 39
        s[ind++] = str3V

        for (i in strGsm.indices) {
            s[ind++] = strGsm[i]
        }
        s[ind++] = strTcp
        s[ind++] = strVers
        s[ind++] = strEvent
        s[ind++] = strTime
        for (i in ind until k) {
            s[i] = "0"
        }
        val s1 = arrayOf<String>()
        for(i in 0 until k){
            s1[i] = s[i] ?: ""
        }
        return s1
    }

    fun fillDevDataByte(obj: Obj) {
        FillConfigPartsFromDb(obj)
        UpdPartStat(obj)
        UpdZoneStat(obj)
        UpdZoneAlarm(obj)
    }

    //================================================================

    fun UpdConfigPart(obj: Obj, part: Int) {
        //checkLenConfigPart(part)
        fillConfigPart(obj, part)
    }

    fun FillConfigPartsFromDb(obj : Obj) {
        for (part in 0 until NUMBER_PART) {
            UpdConfigPart(obj, part)
        }
        //config = true
    }

    fun UpdPartStat(obj : Obj) {
        //checkLenPartStat()
        fillPartStat(obj)
    }

    fun UpdZoneStat(obj : Obj) {
        //checkLenZoneStat()
        fillZoneStat(obj)
    }

    fun UpdZoneAlarm(obj : Obj) {
        //checkLenZoneAlarm()
        fillZoneAlarm(obj)
    }

    //======================== check length =========================
    fun checkLenConfigPart(part: Int) {
        if (configPart[part] == null) configPart[part] = "0000000000000000"
        var s = configPart[part]
        val k = s!!.length
        if (k < 16) {
            val s1 = "0000000000000000"
            s = s + s1.substring(k, 16)
            configPart[part] = s
        }
    }
    fun checkLenPartStat() {
        if (strPartStat == null) strPartStat = "00000000000000000000000000000000"
        var s = strPartStat
        val k = s.length
        if (k < 32) {
            val s1 = "00000000000000000000000000000000"
            s = s + s1.substring(k, 32)
            strPartStat = s
        }
    }
    fun checkLenZoneStat() {
        if (strZoneStat == null) strZoneStat = "0000000000000000"
        var s = strZoneStat
        val k = s.length
        if (k < 16) {
            val s1 = "0000000000000000"
            s = s + s1.substring(k, 16)
            strZoneStat = s
        }
    }
    fun checkLenZoneAlarm() {
        if (strZoneAlarm == null) strZoneAlarm = "0000000000000000"
        var s = strZoneAlarm
        val k = s.length
        if (k < 16) {
            val s1 = "0000000000000000"
            s = s + s1.substring(k, 16)
            strZoneAlarm = s
        }
    }


    //=========================================== fill data bytes ==============

    fun getStrCfgPart(obj: Obj, p: Int) : String {
        var s:String = ""
        when(p){
            0 -> s = obj.ZoneInPart1
            1 -> s = obj.ZoneInPart2
            2 -> s = obj.ZoneInPart3
            3 -> s = obj.ZoneInPart4
            4 -> s = obj.ZoneInPart5
            5 -> s = obj.ZoneInPart6
            6 -> s = obj.ZoneInPart7
            7 -> s = obj.ZoneInPart8
            8 -> s = obj.ZoneInPart9
            9 -> s = obj.ZoneInPart10
            10 -> s = obj.ZoneInPart11
            11 -> s = obj.ZoneInPart12
            12 -> s = obj.ZoneInPart13
            13 -> s = obj.ZoneInPart14
            14 -> s = obj.ZoneInPart15
            15 -> s = obj.ZoneInPart16
        }
        return s
    }
    fun fillConfigPart(obj: Obj, part: Int) {
        var c: Int
        var p = 0
        val cfgPart : String = getStrCfgPart(obj, part)
        val b = Str.Str2Bin(cfgPart)
        for (i in 0 until b.len) {
            c = b.buf[i].toInt()
            for (j in 0..7) {
                val zone = i * 8 + j
                if (zone >= NUMBER_ZONE) return
                if (c and (1 shl j) != 0)
                    zonePresentInPart[part][zone] = 1
                else
                    zonePresentInPart[part][zone] = 0
            }
            if (c != 0) p = 1
        }
        if (p != 0) partPresent[part] = 1
    }

    fun fillPartStat(obj : Obj) {
        val b = Str.Str2Bin(obj.PartStat)
        for (i in 0 until b.len) {
            if (i >= NUMBER_PART) break
            partStat[i] = b.buf[i].toInt()
        }
    }

    fun fillZoneStat(obj : Obj) {
        var c: Int
        var zone: Int
        val b = Str.Str2Bin(obj.ZoneStat)
        for (i in 0 until b.len) {
            c = b.buf[i].toInt()
            for (j in 0..7) {
                zone = i * 8 + j
                if (zone >= NUMBER_ZONE) break
                if (c and (1 shl j) != 0)
                    zoneStat[zone] = 1
                else
                    zoneStat[zone] = 0
            }
        }
    }

    fun fillZoneAlarm(obj : Obj) {
        var c: Int
        var zone: Int
        var b : Buf = Str.Str2Bin(obj.ZoneAlarm)
        for (i in 0 until b.len) {
            c = b.buf[i].toInt()
            for (j in 0..7) {
                zone = i * 8 + j
                if (zone >= NUMBER_ZONE) break
                if (c and (1 shl j) != 0)
                    zoneAlarm[zone] = 1
                else
                    zoneAlarm[zone] = 0
            }
        }
    }



    //========================================================== obj-part-zone =================

    fun GetStatusZone(zone: Int): Int {
        var s = 0
        if (zoneAlarm[zone] != 0)
            s = 2
        else if (zoneStat[zone] != 0)
            s = 1
        else
            s = 0
        return s
    }

    fun GetStatusPart(part: Int): Int {
        var s = 0
        s = partStat[part]
        return s
    }

    fun GetZones(part: Int): ArrayList<Zone> {
        val zones = ArrayList<Zone>()
        for (i in 0 until NUMBER_ZONE) {
            if (zonePresentInPart[part][i] != 0) {
                val z = Zone(i, GetStatusZone(i))
                zones.add(z)
            }
        }
        return zones
    }

    fun GetParts(): ArrayList<Part> {
        val parts = ArrayList<Part>()
        for (i in 0 until NUMBER_PART) {
            if (partPresent[i] == 0) continue

            val p = Part(i, GetStatusPart(i), GetZones(i))
            parts.add(p)
        }
        return parts
    }

    //
    fun getObjStatList(): ArrayList<StatList> {
        val objPartZone = ObjPartZone(GetParts())

        val listStat = ArrayList<StatList>()

        val np = objPartZone.part.size
        Logm.aa("nparts:"  + " " + np.toString())
        for (p in 0 until np) {
            val pp = objPartZone.part.get(p)
            val dp = StatList(1, pp.number, pp.statInt, pp.strStat, pp.cmdArm, pp.color, "")
            listStat.add(dp)
            val nz = objPartZone.part.get(p).zone.size
            for (z in 0 until nz) {
                val zz = objPartZone.part.get(p).zone.get(z)
                val dz = StatList(0, zz.number, zz.statInt, zz.strStat, "", zz.color, "")
                listStat.add(dz)
            }
        }
        return listStat

    }


    //========================


}