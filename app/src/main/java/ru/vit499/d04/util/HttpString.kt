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

//internal var headPost = "POST /andr2/up_tk.php HTTP/1.1\r\n" +
//        "Host: vit499.ru\r\n" +
//        "Content-Type: application/x-www-form-urlencoded;  charset=utf-8\r\n" +
//        "Connection: Close\r\n" +
//        "Content-Length: " //24\r\n\r\n";
//"p1=9999999999999&p2=3333";
internal var pn = "\r\n\r\n"
internal var p1 = "p1="
internal var p2 = "&p2=" //"&p2=3344";
internal var p3 = "&p3=1"
internal var p4 = "&p4="
internal var p5 = "&p5=xxxx"

internal var h1 = "POST /andr2/up_tk"
//internal var h11 = "andr2/up_tk"
internal var h2 = " HTTP/1.1" //Host: ";
internal var h21 = "\r\nHost: "
internal var h3 = "\r\n" +
        "Content-Type: application/x-www-form-urlencoded;  charset=utf-8\r\n" +
        "Connection: Close\r\n" +
        "Content-Length: "

fun getStrArr (arr: ArrayList<String>) : String {
    val sb = StringBuilder()
    val sz = arr.size
    for(i in 0 until sz){
        sb.append((arr.get(i)))
        if(i < (sz-1)) sb.append(',')
    }
    val s = sb.toString()
    Logm.aa("obj: $s")
    return sb.toString()
}
//
fun strSendToken (arr: ArrayList<String>, strId: String) : String {
    val sb = StringBuilder()
    val sbC = StringBuilder()   // sbContent

    val user = Account.accUser
    val pass = Account.accPass
    val serv = Account.accServ
    val us = user + ":" + pass
    val auth = Base64.encodeToString(us.toByteArray(), Base64.NO_WRAP)
    val strObjs = getStrArr(arr)

//    var lenContent = p1.length + strId.length + p2.length + strObjs.length
//    lenContent += p3.length
//    lenContent += p4.length + user.length
//    lenContent += p5.length
//    val strLenContent : String = lenContent.toString()  // ?

    sbC.append(p1);
    sbC.append(strId)
    sbC.append(p2)
    sbC.append(strObjs)
    sbC.append(p3)
    sbC.append(p4)
    sbC.append(user)
    sbC.append(p5)
    val strContent = sbC.toString()
    val lenContent = strContent.length
    val strLenContent = lenContent.toString()

    sb.append(h1)
    sb.append(h2)
    sb.append(strAuth)
    sb.append(auth)
    sb.append(h21)
    sb.append(serv)
    sb.append(h3)
    sb.append(strLenContent)
    sb.append(pn)
    sb.append(strContent)
    val strPost = sb.toString()

    return strPost
}
    // online.navigard.ru/device/NNNN/events
fun strReqHttp (numObj: String, req: String) : String {
    val sb = StringBuilder()

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