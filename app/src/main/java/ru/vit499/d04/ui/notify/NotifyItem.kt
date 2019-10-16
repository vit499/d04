package ru.vit499.d04.ui.notify

class NotifyItem(cid: String, _time: String) {

    val S_EV_END = 8

    private var descr: String = ""
    private var code: String = ""
    private var time: String = ""
    private var color: Int = 0

    init {
        code = cid
        time = _time

        descr = GetDescr(code)

        color = GetColorEv(0)
    }

    internal fun getDescr(): String {
        return descr
    }

    internal fun getCode(): String {
        return code
    }

    //String getTime() { return(time); }
    internal fun getColor(): Int {
        return color
    }

    internal fun GetColorEv(p: Int): Int {
        var p = p
        val c = intArrayOf(
            -0x3f3f40, // не акт. выключен
            -0x6700, // не акт. включен
            -0xff0100, // готов          зеленый
            -0xcd00, // на охране      красный
            -0x33cc67, -0x340000, // тревога
            -0x340000, // тревога
            -0x9901, -0x9901, // принуд.охрана
            -0x9f1000, -0x9f1000, -0x9f1000
        )
        if (p >= S_EV_END) p = 0
        return c[p]
    }

    // 20190207221736
    // 02/07/2019 22:17:36
    internal fun getTime(): String {
        if (time.length < 14) {
            return "x"
        }
        var s = time.substring(6, 8)
        s = s + "/" + time.substring(4, 6)
        s = s + "/" + time.substring(0, 4)
        s = s + " " + time.substring(8, 10)
        s = s + ":" + time.substring(10, 12)
        s = s + ":" + time.substring(12, 14)
        return s
    }

    internal fun GetDescr(code: String): String {
        if (code.length < 13) return code
        val cid = code.substring(4, 8)
        var part = code.substring(8, 10)
        var zone = code.substring(10, 13)
        if (zone.substring(0, 2) == "00")
            zone = zone.substring(2, 3)
        else if (zone.substring(0, 1) == "0") zone = zone.substring(1, 3)
        if (part.substring(0, 1) == "0") part = part.substring(1, 2)
        //int z = Integer.getInteger(zone);
        //zone = String.valueOf(z);
        //int p = Integer.getInteger(part);
        //part = String.valueOf(p);
        var s = "xx"

        if (cid == "E130" || cid == "E131") {
            s = "Тревога зона $zone раздел $part"
            //color = 0xffff3300;
        } else if (cid == "R130") {
            s = "Восстановление зона $zone раздел $part"
        } else if (cid == "E715") {
            s = "Включен выход $zone"
        } else if (cid == "R715") {
            s = "Выключен выход $zone"
        } else if (cid == "R401") {
            s = "Постановка польз. $zone раздел $part"
        } else if (cid == "E401") {
            s = "Снятие польз. $zone раздел $part"
        } else if (cid == "R407") {
            s = "Постановка удаленная польз. $zone раздел $part"
        } else if (cid == "E407") {
            s = "Снятие удаленное польз. $zone раздел $part"
        } else if (cid == "E602") {
            s = "Периодический тест"
        } else if (cid == "E702") {
            s = "Включение"
        } else if (cid == "E301") {
            s = "Неисправность 220В"
        } else if (cid == "R301") {
            s = "Восстановление 220В"
        } else if (cid == "E302") {
            s = "Неисправность 220В"
        } else if (cid == "R302") {
            s = "Восстановление 220В"
        } else if (cid == "E321") {
            s = "Неисправность сирены"
        } else if (cid == "R321") {
            s = "Восстановление сирены"
        } else if (cid == "E312") {
            s = "Неисправность 12VOUT"
        } else if (cid == "R312") {
            s = "Восстановление 12VOUT"
        } else if (cid == "E137") {
            s = "Неисправность тампера"
        } else if (cid == "R137") {
            s = "Восстановление тампера"
        } else if (cid == "E062") {
            s = "Неисправность ENET"
        } else if (cid == "R062") {
            s = "Восстановление ENET"
        } else if (cid == "E704") {
            s = "Синхронизация времени"
        } else if (cid == "E708") {
            s = "Неисправность симкарты$zone"
        } else if (cid == "E628") {
            s = "Программирование завершено"
        } else if (cid == "R301") {
            s = "Восстановление 220В"
        } else
            s = code

        return s
    }
}