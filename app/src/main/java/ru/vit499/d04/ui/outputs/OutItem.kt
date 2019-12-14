package ru.vit499.d04.ui.outputs

import android.content.Context
import android.graphics.Color
import ru.vit499.d04.R
import ru.vit499.d04.util.Colors
import ru.vit499.d04.util.Logm
import ru.vit499.d04.util.Str

data class OutItem(
    val num: Int,
    val funct1: Int,    // функция выхода
    val stat1: Int,
    val temp1: Int,     // температура переключения
    val indTemp1: Int,  // номер датчика температуры
    val factTemp1: Int  // фактическая температура
) {

    val NUMBER_OUT  = 32

    var number: Int = 0
    var stat: Int = 0
    var ftout: Int = 0
    var funct: Int = 0
    var indTemp: Int = 0
    var factTemp: Int = 0

    init{
        number = num
        stat = stat1
        ftout = temp1
        funct = funct1
        indTemp = indTemp1
        factTemp = factTemp1
//        var byteArr = ByteArray(10)
//        byteArr[0] = factTemp.toByte()
//        val strHex = Str.Companion.hex2str(byteArr, 1)
//        Logm.aa("fact temp $number = $strHex")
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
//        val c = longArrayOf(
//            0xff80cbc4, //  включен
//            0xff00cd00  //
//        )
//        if (p >= c.size) p = 0
//        return c[p].toInt()
        return when(stat){
            0 -> Colors.LightCyan
            //1 -> Colors.LightGreen
            1 -> Colors.SpringGreen
            //1 -> Colors.Lime
            2 -> Colors.AquaCyan
            3 -> Colors.CornflowerBlue
            else -> Colors.DarkGray
        }
    }

    fun getState() : String {
        return when(stat){
            0 -> "выключен"
            1 -> "включен"
            else -> "включен"
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
    fun getTemperature() : String {

        if(factTemp != 0x80) return factTemp.toString()
        else return "-"
    }
    fun getFtout() : String {
        return ftout.toString()
    }
    fun isRemote() : Boolean {
        return when(funct){
            FOUT_REMOTE -> true
            FOUT_TIMETABLE -> true
            FOUT_ZONE -> true
            else -> false
        }
    }
    fun IsTermo() : Boolean {
        return when(funct){
            FOUT_TERMO_DOWN -> true
            FOUT_TERMO_UP -> true
            else -> false
        }
    }
    fun getTextTermo(context: Context) : String {
        return when(funct){
            FOUT_TERMO_DOWN -> context.getString(R.string.funct_out_termodown)
            FOUT_TERMO_UP -> context.getString(R.string.funct_out_termoup)
            else -> ""
        }
    }

    fun getStrIndTemper () : String {
        val s = indTemp.toString()
        val s1 = when(indTemp){
            0 -> " (на клемме T)"
            1 -> " (на клемме CLK)"
            2 -> " (на клемме DATA)"
            4,5,6,7 -> " (на 85xx адр${indTemp-3})"
            else -> " (на 8108 адр${(indTemp-8)/4 + 1} IN${(indTemp-8)%4 + 1})"
        }
        val s2 = "Датчик " + s + s1
        return s2
    }
}