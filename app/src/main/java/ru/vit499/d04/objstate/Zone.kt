package ru.vit499.d04.objstate

class Zone(num: Int, stat: Int) {

    internal val S_Z_END = 4

    internal var number: Int = 0
    internal var statInt: Int = 0
    internal var strStat: String = ""
    internal var color: Int = 0

    internal var s1 = arrayOf(
        "В норме", // 0
        "Нарушена", // 1
        "Тревога", // 2
        "-"
    )//

    init {
        number = num
        statInt = stat
        strStat = GetStrStatusZone(stat)
        color = GetColor(stat)
    }

    internal fun GetStrStatusZone(p: Int): String {
        var p = p
        if (p >= 3) p = 0
        return s1[p]
    }

    internal fun GetColor(p: Int): Int {
        var p = p
        val c = intArrayOf(
            -0xff0100, //          зеленый
            -0xcd00, //       красный
            -0x340000, // тревога
            -0x340000, // тревога
            -0x9901, -0x9901, // принуд.охрана
            -0x9f1000, -0x9f1000, -0x9f1000
        )
        if (p >= S_Z_END) p = 0
        return c[p]
    }
}