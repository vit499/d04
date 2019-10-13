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