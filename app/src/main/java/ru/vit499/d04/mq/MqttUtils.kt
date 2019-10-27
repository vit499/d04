package ru.vit499.d04.mq

import ru.vit499.d04.ui.misc.Account
import ru.vit499.d04.util.Buf
import ru.vit499.d04.util.Logm
import java.util.*


fun RecConnect(b: ByteArray, len_src: Int) : String? {
    Logm.aa(b, len_src)
    var res : String? = null
    val type = (b[0].toInt() shr 4) and 0x0f
    if (type == 2) { // answer connect 0x20  0x02  0x00  0x00
        if (b[3].toInt() == 0) {
            res = "ok"
        } else {
            res = "error"
        }
    }
    return res
}

fun RecSub(b: ByteArray, len_src: Int) : String? {
    Logm.aa(b, len_src)
    var res : String? = null
    var r : Int
    if(len_src < 5) return null
    val type = (b[0].toInt() shr 4) and 0x0f

    if (type == 9) { // answer subscribe 0x90  0x03  0x00  0x01  0x01
        r = b[4].toInt()
        if (r and (1 shl 7) == 0) {
            res = "ok"
        } else {
            res = "error"
        }

    }
    return res
}

fun RecPub(b: ByteArray, len_src: Int) : String? {
    var r = 0
    Logm.aa("rec pub")
    Logm.aa(b, len_src)
    val type = (b[0].toInt() shr 4) and 0x0f

    if (type != 0x0d) {
        //Logm.Log(1, " rec:")
        //Logm.Log(b, len_src)
    }
    //Logm.Log("type:" + String.valueOf(type));

    if (type == 3) {
        //Logm.Log(b, len_src);
        //val rp = RecPub()
        //val pId = rp.RecPublish(b, len_src)
        //if (pId != 0);
        //AckQueue.AddMes(pId)
    }
    return null
}

fun RecMqtt(b: ByteArray, len_src: Int) : String? {

    return null
}

//=====================================

// 0x10 0x29 0x00 0x06 MQIsdp 0x03 0xC2 0x00 0x0A 0x00 0x0F imei 0x00 0x07 Dev4444 0x00 0x04 2222
fun FillConnectMqtt(): Buf {
    //appSet.Pref();
    val mqtt = "MQIsdp"
    val b = Buf()
    var len_pack: Int
    var f: Int
    var im = (1..0xffffff).random()
    val si = im.toString() + "234554321234565345"
    val imei = si.substring(0, 15)
    val len_mqtt = mqtt.length
    val len_id = imei.length

    val user = Account.accUser
    val pass = Account.accPass
    val len_user = user.length
    val len_pass = pass.length

    len_pack = len_mqtt + len_id + len_user + len_pass + 8
    if (len_user != 0) len_pack += 2
    if (len_pass != 0) len_pack += 2
    b.Add(0x10)
    b.Add(len_pack)
    b.Add(0)
    b.Add(len_mqtt)
    b.Cpy(mqtt)

    b.Add(0x03) //dst[len++] = 0x03;

    f = 0x02
    if (len_user != 0) f = f or (1 shl 7)
    if (len_pass != 0) f = f or (1 shl 6)
    b.Add(f) // dst[len++] = f;
    b.Add(0x02) //dst[len++] = 0x02;  // keep-alive
    b.Add(0x58) // dst[len++] = 0x58;  // 0x0258 = 10min
    b.Add(0) //dst[len++] = 0;
    b.Add(len_id) //dst[len++] = len_id;
    b.Cpy(imei)
    if (len_user != 0) {
        b.Add(0)
        b.Add(len_user)
        b.Cpy(user)
    }
    if (len_pass != 0) {
        b.Add(0)
        b.Add(len_pass)
        b.Cpy(pass)
    }

    return b
}

// 0x82 0x07 0x00 0x01 0x00 0x02 a1 0x01     - qos = 1
// 0x82, len_mes,  id,id, 0x00,len_topic,  topic, qos
fun FillSubMqtt(numObj: String): Buf {
    val b = Buf()
    val len = 0
    var len_pack = 0
    var len_topic = 0
    val qos = 1
    val topicSub: String

    val user = Account.accUser
    val packId = (1..0xffff).random()

    topicSub = "$user/$numObj/devsend/#"
    len_topic = topicSub.length
    len_pack = len_topic + 5

    b.Add(0x82)
    b.Add(len_pack)
    b.Add((packId shr 8) and 0xff)
    b.Add(packId and 0xff)
    b.Add(0)
    b.Add(len_topic)
    b.Cpy(topicSub)
    b.Add(qos)
    return b
}

//==============================================

fun correctSout(s: String): String {
    var s1 = s

    val k = s1.length
    if (k > 8) s1 = s1.substring(0, 8)
    if (k < 8) {
        val s2 = "00000000"
        s1 = s1 + s2.substring(k, 8)
    }
    return s1
}
fun MesStatusOut(id: Int, s1: String): Int {
    var r = 0

    return r
}