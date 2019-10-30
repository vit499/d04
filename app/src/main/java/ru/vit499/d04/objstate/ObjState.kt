package ru.vit499.d04.objstate

import ru.vit499.d04.database.Obj
import ru.vit499.d04.ui.main.StatusItem
import ru.vit499.d04.ui.outputs.OutItem
import ru.vit499.d04.util.Buf
import ru.vit499.d04.util.Logm
import ru.vit499.d04.util.Str

class ObjState(obj: Obj) {

    val NUMBER_ZONE = 64
    val NUMBER_PART = 16
    val NUMBER_OUT = 32
    val NUMBER_TEMP = 13

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

    fun getStrCfgPart(obj: Obj, p: Int) : String? {
        return when(p){
            0 -> obj.ZoneInPart1
            1 -> obj.ZoneInPart2
            2 -> obj.ZoneInPart3
            3 -> obj.ZoneInPart4
            4 -> obj.ZoneInPart5
            5 -> obj.ZoneInPart6
            6 -> obj.ZoneInPart7
            7 -> obj.ZoneInPart8
            8 -> obj.ZoneInPart9
            9 -> obj.ZoneInPart10
            10 -> obj.ZoneInPart11
            11 -> obj.ZoneInPart12
            12 -> obj.ZoneInPart13
            13 -> obj.ZoneInPart14
            14 -> obj.ZoneInPart15
            15 -> obj.ZoneInPart16
            else -> null
        }
    }
    fun fillConfigPart(obj: Obj, part: Int) {
        var c: Int
        var p = 0
        val cfgPart : String? = getStrCfgPart(obj, part)
        if(cfgPart == null) return
        val b = Str.Str2Bin(cfgPart!!)
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

    //  из объекта, который представляет из себя список разделов, а те - списки зон
    // формируется список разделов-зон - StatusItem
    fun getObjStatList(): ArrayList<StatusItem> {
        val objPartZone = ObjPartZone(GetParts())

        val listStat = ArrayList<StatusItem>()

        val np = objPartZone.part.size
        Logm.aa("nparts:"  + " " + np.toString())
        for (p in 0 until np) {
            val pp = objPartZone.part.get(p)
            val dp = StatusItem(1, pp.number, pp.statInt, pp.strStat, pp.cmdArm, pp.color, "")
            listStat.add(dp)
            val nz = objPartZone.part.get(p).zone.size
            for (z in 0 until nz) {
                val zz = objPartZone.part.get(p).zone.get(z)
                val dz = StatusItem(0, zz.number, zz.statInt, zz.strStat, "", zz.color, "")
                listStat.add(dz)
            }
        }
        return listStat

    }


    //========================


}