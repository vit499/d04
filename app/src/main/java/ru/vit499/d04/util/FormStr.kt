package ru.vit499.d04.util

import ru.vit499.d04.database.Obj
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*


fun formStrObj (obj : Obj?) : String {
    val sb = StringBuilder()
    sb.append("obj: ")
    obj?.let {
        sb.append(obj.objName)
        sb.append(" ")
        sb.append(obj.objDescr)
    } ?: sb.append("not found")

    val s : String = String(sb)
    return s
}

// 10:21:33_
fun getTime(): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return sdf.format(Date()) + " "
}
fun getTime1(src: String) : String {
    if (src.length < 14) {
        return "x"
    }
    var s = src.substring(6, 8)
    s = s + "/" + src.substring(4, 6)
    s = s + "/" + src.substring(0, 4)
    s = s + " " + src.substring(8, 10)
    s = s + ":" + src.substring(10, 12)
    s = s + ":" + src.substring(12, 14)
    return s
}

fun getStrInfo(obj: Obj) : String {
    val sb = StringBuilder()
    sb.append("Версия:")
    sb.append("\t\t")
    sb.append(obj.vers)
    sb.append("\r\n")

    sb.append("Temp:")
    sb.append("\t\t")
    sb.append(obj.temp0)
    sb.append("\r\n")

    sb.append("gsm:")
    sb.append("\t\t")
    sb.append(obj.gsm1)
    sb.append("\r\n")

    sb.append("U:")
    sb.append("\t\t")
    sb.append(obj.dv12v)
    sb.append("\r\n")

    sb.append("tcp:")
    sb.append("\t\t")
    sb.append(obj.tcp)
    sb.append("\r\n")

    sb.append("time:")
    sb.append("\t\t")
    sb.append(obj.objTime)
    sb.append("\r\n")

    return sb.toString()
}

fun getArrStrInfo(obj: Obj) : ArrayList<String> {
    val sb1 = StringBuilder()
    val sb2 = StringBuilder()

    sb1.append("Версия:")
    sb1.append("\r\n")
    sb2.append(obj.vers)
    sb2.append("\r\n")

    sb1.append("Температура:")
    sb1.append("\r\n")
    sb2.append(obj.temp0)
    sb2.append("\r\n")

    sb1.append("Уровень gsm:")
    sb1.append("\r\n")
    sb2.append(obj.gsm1)
    sb2.append("\r\n")

    sb1.append("Uпитания:")
    sb1.append("\r\n")
    sb2.append(obj.dv12v)
    sb2.append("\r\n")

    sb1.append("tcp:")
    sb1.append("\r\n")
    sb2.append(obj.tcp)
    sb2.append("\r\n")

    sb1.append("time:")
    sb1.append("\r\n")
    //sb2.append(obj.objTime)
    val t = getTime1(obj.objTime)
    sb2.append(t)
    sb2.append("\r\n")

    val arr = ArrayList<String>()
    val s1 = sb1.toString()
    val s2 = sb2.toString()
    arr.add(s1)
    arr.add(s2)
    return arr
}
