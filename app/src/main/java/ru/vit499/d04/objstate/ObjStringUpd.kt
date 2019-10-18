package ru.vit499.d04.objstate

import ru.vit499.d04.database.Obj
import ru.vit499.d04.util.Buf
import ru.vit499.d04.util.Logm
import ru.vit499.d04.util.Str

class ObjStringUpd(var obj: Obj) {

    val NUMBER_ZONE = 64
    val NUMBER_PART = 16
    val NUMBER_OUT = 32
    val NUMBER_TEMP = 13

    internal var ZoneInPart = arrayOfNulls<String>(NUMBER_PART)
    internal var temp = arrayOfNulls<String>(NUMBER_TEMP)
    internal var gsm = arrayOfNulls<String>(2)

    internal var ZoneStat: String = ""
    internal var ZoneAlarm: String = ""
    internal var PartStat: String = ""
    internal var fout: String = ""
    internal var ftout: String = ""
    internal var sout: String = ""
    internal var trbl: String = ""
    internal var objEvent: String = ""
    internal var dv12v: String = ""
    internal var dv3v: String = ""
    internal var tcp: String = ""
    internal var vers: String = ""
    internal var timeLastData: String = ""
    internal var objTime: String = ""

    init {
        setObjUpd(obj)


    }

    fun setObjUpd(obj : Obj) {

        ZoneInPart[0] = obj.ZoneInPart1
        ZoneInPart[1] = obj.ZoneInPart2
        ZoneInPart[2] = obj.ZoneInPart3
        ZoneInPart[3] = obj.ZoneInPart4
        ZoneInPart[4] = obj.ZoneInPart5
        ZoneInPart[5] = obj.ZoneInPart6
        ZoneInPart[6] = obj.ZoneInPart7
        ZoneInPart[7] = obj.ZoneInPart8
        ZoneInPart[8] = obj.ZoneInPart9
        ZoneInPart[9] = obj.ZoneInPart10
        ZoneInPart[10] = obj.ZoneInPart11
        ZoneInPart[11] = obj.ZoneInPart12
        ZoneInPart[12] = obj.ZoneInPart13
        ZoneInPart[13] = obj.ZoneInPart14
        ZoneInPart[14] = obj.ZoneInPart15
        ZoneInPart[15] = obj.ZoneInPart16

        ZoneStat = obj.ZoneStat
        ZoneAlarm = obj.ZoneAlarm
        PartStat = obj.PartStat
        fout = obj.fout
        ftout = obj.ftout
        sout = obj.sout
        trbl = obj.trbl

        temp[0] = obj.temp0
        temp[1] = obj.temp1
        temp[2] = obj.temp2
        temp[3] = obj.temp3
        temp[4] = obj.temp4
        temp[5] = obj.temp5
        temp[6] = obj.temp6
        temp[7] = obj.temp7
        temp[8] = obj.temp8

        dv12v = obj.dv12v
        dv3v = obj.dv3v
        gsm[0] = obj.gsm1
        gsm[1] = obj.gsm2
        tcp = obj.tcp
        vers = obj.vers

        objEvent = obj.objEvent
        objTime = obj.objTime

    }
    fun getObjUpd() : Obj {
        obj.ZoneInPart1 = ZoneInPart[0] ?: "0000000000000000"
        obj.ZoneInPart2 = ZoneInPart[1] ?: "0000000000000000"
        obj.ZoneInPart3 = ZoneInPart[2] ?: "0000000000000000"
        obj.ZoneInPart4 = ZoneInPart[3] ?: "0000000000000000"
        obj.ZoneInPart5 = ZoneInPart[4] ?: "0000000000000000"
        obj.ZoneInPart6 = ZoneInPart[5] ?: "0000000000000000"
        obj.ZoneInPart7 = ZoneInPart[6] ?: "0000000000000000"
        obj.ZoneInPart8 = ZoneInPart[7] ?: "0000000000000000"
        obj.ZoneInPart9 = ZoneInPart[8] ?: "0000000000000000"
        obj.ZoneInPart10 = ZoneInPart[9] ?: "0000000000000000"
        obj.ZoneInPart11 = ZoneInPart[10] ?: "0000000000000000"
        obj.ZoneInPart12 = ZoneInPart[11] ?: "0000000000000000"
        obj.ZoneInPart13 = ZoneInPart[12] ?: "0000000000000000"
        obj.ZoneInPart14 = ZoneInPart[13] ?: "0000000000000000"
        obj.ZoneInPart15 = ZoneInPart[14] ?: "0000000000000000"
        obj.ZoneInPart16 = ZoneInPart[15] ?: "0000000000000000"

        obj.ZoneStat = ZoneStat
        obj.ZoneAlarm = ZoneAlarm
        obj.PartStat = PartStat
        obj.fout = fout
        obj.ftout = ftout
        obj.sout = sout
        obj.trbl = trbl

        obj.temp0 = temp[0] ?: ""
        obj.temp1 = temp[1] ?: ""
        obj.temp2 = temp[2] ?: ""
        obj.temp3 = temp[3] ?: ""
        obj.temp4 = temp[4] ?: ""
        obj.temp5 = temp[5] ?: ""
        obj.temp6 = temp[6] ?: ""
        obj.temp7 = temp[7] ?: ""
        obj.temp8 = temp[8] ?: ""
        obj.gsm1 = gsm[0] ?: ""
        obj.gsm2 = gsm[1] ?: ""

        obj.dv12v = dv12v
        obj.dv3v = dv3v
        obj.tcp = tcp
        obj.vers = vers

        obj.objEvent = objEvent
        obj.objTime = objTime

        return obj
    }

    //======================== check length =========================
    fun checkLenConfigPart(part: Int) {
        if (ZoneInPart[part] == null) ZoneInPart[part] = "0000000000000000"
        var s = ZoneInPart[part]
        val k = s!!.length
        if (k < 16) {
            val s1 = "0000000000000000"
            s = s + s1.substring(k, 16)
            ZoneInPart[part] = s
        }
    }

    fun checkLenPartStat() {
        if (PartStat == null) PartStat = "00000000000000000000000000000000"
        var s = PartStat
        val k = s.length
        if (k < 32) {
            val s1 = "00000000000000000000000000000000"
            s = s + s1.substring(k, 32)
            PartStat = s
        }
    }

    fun checkLenZoneStat() {
        if (ZoneStat == null) ZoneStat = "0000000000000000"
        var s = ZoneStat
        val k = s.length
        if (k < 16) {
            val s1 = "0000000000000000"
            s = s + s1.substring(k, 16)
            ZoneStat = s
        }
    }

    fun checkLenZoneAlarm() {
        if (ZoneAlarm == null) ZoneAlarm = "0000000000000000"
        var s = ZoneAlarm
        val k = s.length
        if (k < 16) {
            val s1 = "0000000000000000"
            s = s + s1.substring(k, 16)
            ZoneAlarm = s
        }
    }

    //-----------------------------------------------------------

    fun UpdConfigPart(part: Int, s: String?) {
        if (s != null) ZoneInPart[part] = s          // init from mqtt or http
        checkLenConfigPart(part)
        //fillConfigPart(part)
    }
    fun UpdPartStat(s: String?) {
        if (s != null) PartStat = s
        checkLenPartStat()
        //fillPartStat()
    }

    fun UpdZoneStat(s: String?) {
        if (s != null) ZoneStat = s
        checkLenZoneStat()
        //fillZoneStat()
    }

    fun UpdZoneAlarm(s: String?) {
        if (s != null) ZoneAlarm = s
        checkLenZoneAlarm()
        //fillZoneAlarm()
    }

    fun UpdTemper(t: Int, s: String?) {
        if (s != null) temp[t] = s
    }

    fun UpdGsm(g: Int, s: String?) {
        if (s != null) gsm[g] = s
    }

    fun Upd12v(s: String) {
        if (s != null) dv12v = s
    }

    fun UpdTcp(s: String) {
        if (s != null) tcp = s
    }

    fun UpdVers(s: String) {
        if (s != null) vers = s
    }

    fun UpdTime(s: String) {
        if (s != null) objTime = s
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
            if (cmd == s[t + 42]) {
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
        else if (cmd == s[40])
            Upd12v(value)
        else if (cmd == s[44])
            UpdTcp(value)
        else if (cmd == s[45])
            UpdVers(value)
        else if (cmd == s[47]) UpdTime(value)
    }

    fun UpdStringAll(list: ArrayList<Buf>) : Obj {
        var k = list.size

        for(i in 0 until k){
            Upd(list.get(i).buf, list.get(i).len)
        }

        val obj = getObjUpd()
        return obj
    }
}