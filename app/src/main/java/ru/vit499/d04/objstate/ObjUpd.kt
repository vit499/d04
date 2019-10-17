package ru.vit499.d04.objstate

import ru.vit499.d04.database.Obj
import ru.vit499.d04.util.Buf
import ru.vit499.d04.util.Str

class ObjUpd(var obj: Obj) {

    val NUMBER_ZONE = 64
    val NUMBER_PART = 16
    val NUMBER_OUT = 32
    val NUMBER_TEMP = 12

    internal var configPart = arrayOfNulls<String>(NUMBER_PART)
    internal var strTemp = arrayOfNulls<String>(NUMBER_TEMP)
    internal var strGsm = arrayOfNulls<String>(2)

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
        setObjUpd(obj)


    }

    fun setObjUpd(obj : Obj) {

        configPart[0] = obj.ZoneInPart1
        configPart[1] = obj.ZoneInPart2
        configPart[2] = obj.ZoneInPart3
        configPart[3] = obj.ZoneInPart4
        configPart[4] = obj.ZoneInPart5
        configPart[5] = obj.ZoneInPart6
        configPart[6] = obj.ZoneInPart7
        configPart[7] = obj.ZoneInPart8
        configPart[8] = obj.ZoneInPart9
        configPart[9] = obj.ZoneInPart10
        configPart[10] = obj.ZoneInPart11
        configPart[11] = obj.ZoneInPart12
        configPart[12] = obj.ZoneInPart13
        configPart[13] = obj.ZoneInPart14
        configPart[14] = obj.ZoneInPart15
        configPart[15] = obj.ZoneInPart16

    }
    fun getObjUpd() : Obj {
        obj.ZoneInPart1 = configPart[0] ?: "0000000000000000"
        obj.ZoneInPart2 = configPart[1] ?: "0000000000000000"
        obj.ZoneInPart3 = configPart[2] ?: "0000000000000000"
        obj.ZoneInPart4 = configPart[3] ?: "0000000000000000"
        obj.ZoneInPart5 = configPart[4] ?: "0000000000000000"
        obj.ZoneInPart6 = configPart[5] ?: "0000000000000000"
        obj.ZoneInPart7 = configPart[6] ?: "0000000000000000"
        obj.ZoneInPart8 = configPart[7] ?: "0000000000000000"
        obj.ZoneInPart9 = configPart[8] ?: "0000000000000000"
        obj.ZoneInPart10 = configPart[9] ?: "0000000000000000"
        obj.ZoneInPart11 = configPart[10] ?: "0000000000000000"
        obj.ZoneInPart12 = configPart[11] ?: "0000000000000000"
        obj.ZoneInPart13 = configPart[12] ?: "0000000000000000"
        obj.ZoneInPart14 = configPart[13] ?: "0000000000000000"
        obj.ZoneInPart15 = configPart[14] ?: "0000000000000000"
        obj.ZoneInPart16 = configPart[15] ?: "0000000000000000"



        return obj
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

    //-----------------------------------------------------------

    fun UpdConfigPart(part: Int, s: String?) {
        if (s != null) configPart[part] = s          // init from mqtt or http
        checkLenConfigPart(part)
        //fillConfigPart(part)
    }
    fun UpdPartStat(s: String?) {
        if (s != null) strPartStat = s
        checkLenPartStat()
        //fillPartStat()
    }

    fun UpdZoneStat(s: String?) {
        if (s != null) strZoneStat = s
        checkLenZoneStat()
        //fillZoneStat()
    }

    fun UpdZoneAlarm(s: String?) {
        if (s != null) strZoneAlarm = s
        checkLenZoneAlarm()
        //fillZoneAlarm()
    }

    fun UpdTemper(t: Int, s: String?) {
        if (s != null) strTemp[t] = s
    }

    fun UpdGsm(g: Int, s: String?) {
        if (s != null) strGsm[g] = s
    }

    fun Upd12v(s: String) {
        if (s != null) str12V = s
    }

    fun UpdTcp(s: String) {
        if (s != null) strTcp = s
    }

    fun UpdVers(s: String) {
        if (s != null) strVers = s
    }

    fun UpdTime(s: String) {
        if (s != null) strTime = s
    }

    //------------------------------------------


    fun Upd(buf: ByteArray, len: Int) {
        val k = Str.indexof(buf, 0, '='.toByte(), 1, len)
        if (k == -1) return
        val cmd = Str.byte2str(buf, k - 1)
        var value = ""
        if (k < len - 1) value = Str.byte2str(buf, k, len)
        // Logm.Log("cmd=" + cmd);
        // Logm.Log("val=" + val);
        val s = ObjDb.getStrDbColumns()

        for (part in 0 until NUMBER_PART) {
            if (cmd == s[part + 4]) {
                UpdConfigPart(part, value)
                return
            }
        }
        for (t in 0 until NUMBER_TEMP) {
            if (cmd == s[t + 27]) {
                UpdTemper(t, value)
                return
            }
        }
        for (t in 0..1) {
            if (cmd == s[t + 41]) {
                UpdGsm(t, value)
                return
            }
        }
        if (cmd == s[20])
            UpdZoneStat(value)
        else if (cmd == s[21])
            UpdZoneAlarm(value)
        else if (cmd == s[22])
            UpdPartStat(value)
        else if (cmd == s[39])
            Upd12v(value)
        else if (cmd == s[43])
            UpdTcp(value)
        else if (cmd == s[44])
            UpdVers(value)
        else if (cmd == s[46]) UpdTime(value)
    }

    fun UpdAll(list: ArrayList<Buf>) : Obj {
        var k = list.size
        //for(int i = 0; i < list.size(); i++)
            //            obj.Upd(list.get(i).buf, list.get(i).len);
//        }
        for(i in 0 until k){
            Upd(list.get(i).buf, list.get(i).len)
        }

        val obj = getObjUpd()
        return obj
    }
}