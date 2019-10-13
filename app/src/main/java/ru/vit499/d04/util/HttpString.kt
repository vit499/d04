package ru.vit499.d04.util

import android.util.Base64
import ru.vit499.d04.ui.misc.Account
import java.lang.StringBuilder


internal var hg1 = "GET /device/"
internal var hg2 = " HTTP/1.1"
internal var hg21 = "\r\nHost: "
internal var hg3 = "\r\n" +
        "Content-Type: application/x-www-form-urlencoded;  charset=utf-8\r\n" +
        "Connection: Close\r\n"

internal var strAuth = "\r\nAuthorization: Basic "

//String strAuth = "\r\nAuthorization: Basic ";
/*
    online.navigard.ru/device/NNNN/events
     */
fun strReqHttp (numObj: String, req: String) : String {
    val sb = StringBuilder()

    var b : Buf = Buf()

    b.Cpy(hg1)

    val user = Account.accUser
    val pass = Account.accPass
    val serv = Account.accServ
    val us = user + ":" + pass
    val auth = Base64.encodeToString(us.toByteArray(), Base64.NO_WRAP)

    sb.append(hg1)
    sb.append(numObj)
    sb.append('/')
    sb.append(req)
    sb.append(hg2)
    sb.append(strAuth)
    sb.append(auth)
    sb.append(hg21)
    sb.append(serv)
    sb.append(hg3)
    sb.append("\r\n")
    return sb.toString()
}