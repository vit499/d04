package ru.vit499.d04.ui.outputs

import ru.vit499.d04.R

class OutItem(num: Int, funct1: Int, stat1: Int, temp1: Int) {

    val NUMBER_OUT  = 32

    var number: Int = 0
    var stat: Int = 0
    var ftout: Int = 0
    var funct: Int = 0

    init{
        number = num
        stat = stat1
        ftout = temp1
        funct = funct1
    }


    var FOUT_OFF = 0
    var FOUT_STATUS_ARM_ALARM = 1
    var FOUT_STATUS_ARM = 2
    var FOUT_SIREN = 3
    var FOUT_REMOTE = 4
    var FOUT_TRBL_GSM = 5
    var FOUT_TIMETABLE = 6
    var FOUT_TERMO_DOWN = 7
    var FOUT_TERMO_UP = 8
    var FOUT_AFTER_ARM = 9
    var FOUT_AFTER_DISARM = 10
    var FOUT_FIRE_RESET = 11
    var FOUT_ZONE = 12
    var FOUT_PART_ALARM = 13
    var FOUT_END = 14

    private val strFouts = arrayOf(
        "не задействован",
        "статус",
        "статус",
        "сирена",
        "удаленно управляемый",
        "неиспр.GSM",
        "упр. по расписанию",
        "вкл при температуре ниже ",
        "вкл при температура выше ",
        "вкл при постановке на охрану ",
        "вкл при снятии ",
        "сброс пожарных датчиков ", // fire
        "вкл при нарушении зоны ",
        "вкл при тревоге раздела ",
        ""
    )

    internal fun getColorOut(): Int {
        var p = stat
        val c = intArrayOf(
            //-0x3f3f40, // не акт. выключен
            //-0x006700, // не акт. включен
            -0xff0100, // готов          зеленый
            -0x00cd00 // на охране      красный
        )
        if (p >= c.size) p = 0
        return c[p]
    }

    fun getState() : String {
        return when(stat){
            0 -> "выключен"
            1 -> "включен"
            else -> "-"
        }
    }
    fun getNumber(): String {
        return "Выход " + (number+1).toString()
    }
    fun getFunct() : String {
        if(funct >= strFouts.size) funct = 0
        var s = strFouts[funct]
        if(funct == FOUT_TERMO_DOWN) {
            s = s + ftout.toString() + "град"
        }
        else if(funct == FOUT_TERMO_UP) {
            s = s + ftout.toString() + "град"
        }
        return s
    }
    fun getFtout() : String {
        return ftout.toString()
    }
}