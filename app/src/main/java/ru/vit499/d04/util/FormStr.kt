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
