package ru.vit499.d04.http

import ru.vit499.d04.util.Logm
import ru.vit499.d04.util.Str


fun recEvent(buf : ByteArray, len : Int) : String? {
    var s : String? = null

    var a = -1 // = r2.indexOf('[');
    var b = -1 // = r2.indexOf(']', a);
    for (i in 0 until len) {
        if (buf[i] == ']'.toByte()) {
            b = i
            //break;
        }
    }
    for (i in 0 until len) {
        if (buf[i] == '['.toByte()) {
            a = i
            break
        }
    }

    if (a != -1 && b != -1) {
        //Logm.LogAc(buf, (b-100), len);
        s = Str.byte2str(buf, a, b)
        //Logm.aa(s)
        if (s == null || s!!.length < 2) s = null
    }
    return s
}

fun recState (buf: ByteArray, len_src : Int) : String? {
    var s : String? = null
    var a = -1
    var b = -1
    val cntEqMax : Int = 59
    var cntEq : Int = 0
    var len : Int = len_src

    if(len < 100) return null
    val r = String(buf, 0, len)
    //Logm.aa(r)
    if (!Str.checkHttpOk(buf, len)) {
        if (len > 100) len = 100
        val r2 = Str.byte2str(buf, 0, len)
        if (r2 == null || r2.length < 2) return null
       // Logm.aa(r2)
        return "error"
    }
    for (i in 0 until len) {
        if (buf[i] == '='.toByte()) {
            cntEq++
            if (cntEq >= cntEqMax) {
                b = 1
                break
            }
        }
    }
    if (b != -1) {
        s = Str.byte2str(buf, 0, len)
        //Logm.aa("res: $s")
        if (s == null || s.length < 2) return null
        Logm.aa("ok > ")
    }
    else {
        Logm.aa("... ")
    }
    return s
}

fun rec1111 (buf: ByteArray, len : Int) : String? {
    var s = ""

    val r2 = Str.byte2str(buf, len)

    if (r2 == null || r2.length < 2) return null
   // Logm.aa("ht rec: $r2")
    val a = r2.indexOf("111", 0)
    if(a != -1) {
        s = "ok"
    }
    else {
        s = "error"
    }
    return s
}