package ru.vit499.d04.util

import ru.vit499.d04.database.Obj
import java.lang.StringBuilder


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