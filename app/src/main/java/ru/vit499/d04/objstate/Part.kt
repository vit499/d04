package ru.vit499.d04.objstate

import ru.vit499.d04.util.Logm

class Part(num: Int, stat: Int, zones: ArrayList<Zone>) {

    val STATUS_ABSENT : Int = 0
    val STATUS_NOT_ARMED : Int = 1
    val STATUS_TIME_OUT : Int = 2
    val STATUS_ARMED : Int = 4
    val STATUS_TIME_IN : Int = 8
    val STATUS_BYPASS : Int = 0x10
    val STATUS_NOT_READY : Int = 0x20
    val STATUS_ALARM : Int = 0x40
    val STATUS_FORCE : Int = 0x80

    val S_P_READY : Int = 1
    val S_P_TIME_IN : Int = 2
    val S_P_ARMED : Int = 3
    val S_P_TIME_OUT : Int = 4
    val S_P_NOT_READY : Int = 5
    val S_P_ALARM : Int = 6
    val S_P_ALARM_ARMED : Int = 7
    val S_P_BYPASS : Int = 8
    val S_P_FORCE : Int = 9
    val S_P_END : Int = 10

    internal var number: Int = 0
    internal var statInt: Int = 0
    internal var strStat: String = ""
    internal var cmdArm: String = ""
    internal var color: Int = 0
    //List<Zone> zone;
    internal var zone = ArrayList<Zone>()

    init {
        number = num
        statInt = stat
        val p = GetIndStatusPart(stat)
        strStat = GetStrStatusPart(p)
        cmdArm = GetStrCmdArm(p)
        color = GetColorBtnArm(p)
        copyArr(zones)
    }

    fun copyArr (z: ArrayList<Zone>) {
        var k : Int = z.size
        zone.clear()
        for(i in 0 until k){
            zone.add(z.get(i))
        }
    }


    //--------------
    fun GetIndStatusPart(status: Int): Int {
        var s = 0
        val sa: Int    // status armed
        val p: Int
        s = status
        sa = s and (STATUS_NOT_READY or STATUS_ALARM).inv()

        if (s == STATUS_ABSENT) {
            p = 0
        } else if (s and STATUS_ALARM != 0) {
            if (sa == STATUS_NOT_ARMED) {
                p = S_P_ALARM
            } else {
                p = S_P_ALARM_ARMED
            }
        } else if (sa == STATUS_ARMED) {
            p = S_P_ARMED
        } else if (sa == STATUS_ARMED or STATUS_BYPASS) {
            p = S_P_BYPASS
        } else if (sa == STATUS_ARMED or STATUS_BYPASS or STATUS_FORCE) {
            p = S_P_FORCE
        } else if (sa and STATUS_TIME_OUT != 0) {
            p = S_P_TIME_OUT
        } else if (sa and STATUS_TIME_IN != 0) {
            p = S_P_TIME_IN
        } else {
            if (s and STATUS_NOT_READY != 0) {
                p = S_P_NOT_READY
            } else {
                p = S_P_READY
            }
        }
        return p
    }

    //-----------------
    fun GetStrStatusPart(p: Int): String {
        var p = p
        val s = arrayOf(
            "-", // 0
            "Готов", // 1
            "Задержка на вход", // 2
            "На охране", // 3
            "Задержка на выход", // 4
            "Не готов", // 5
            "Тревога (снят)", // 6
            "Тревога (на охране)", // 7
            "Частичная охрана", // 8
            "Принудительная охрана", // 9
            "-"
        )//
        if (p >= S_P_END) p = 0
        return s[p]
    }

    //---
    fun GetStrCmdArm(p: Int): String {
        var p = p
        val s = arrayOf(
            "-", // 0
            "Поставить на охрану", // 1
            "Снять с охраны", // 2
            "Снять с охраны", // 3
            "Снять с охраны", // 4
            "-", // 5
            "Сбросить тревогу", // 6
            "Снять с охраны", // 7
            "Снять с охраны", // 8
            "Снять с охраны", // 9
            "-"
        )//
        if (p >= S_P_END) p = 0
        return s[p]
    }

    fun GetColorBtnArm(p: Int): Int {
        var p = p
        val c = intArrayOf(
            -0x990067, -0xff0100, // готов          зеленый
            -0x33cc67, -0xcd00, // на охране      красный
            -0x33cc67, -0x3f3f40, // не готов
            -0x340000, // тревога
            -0x340000, // тревога
            -0x9901, -0x9901, // принуд.охрана
            -0x9f1000, -0x9f1000, -0x9f1000
        )
        if (p >= S_P_END) p = 0
        return c[p]
    }
}