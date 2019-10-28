package ru.vit499.d04.mq

import ru.vit499.d04.ui.misc.Account
import ru.vit499.d04.util.Buf
import ru.vit499.d04.util.Logm
import ru.vit499.d04.util.Str
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

fun multiTopic(s1 : String) : Map<String, String>? {
    var r = 0
    if (!s1.contains("&")) return null
    var s : List<String>? = null
    try{
        s = s1.split("&")
    }
    catch (ex: Exception){
        return null
    }
    if(s == null) return null
    if(s.size < 1) return null

    val map : MutableMap<String, String> = mutableMapOf()

    for (i in 0 until s.size) {
        val s11 = s[i]
        if (!s11.contains("=")) return null
        var s2 : List<String>? = null
        try {
            s2 = s11.split("=")
            if(s2.size == 2) {
                map.put(s2[0], s2[1])
            }
        }
        catch (ex: Exception){
            return null
        }
    }
    return map
}

// a1021/1021/devsend/topic1/topic2
fun checkTopic(topic: String, mes: String): Map<String, String>? {
    //var r = 0
    //if(len_src < 5) return null
    val s1 = topic    // Str.ByteToStr(b, len_src)

    var s : List<String>? = null
    try{
        s = s1.split("/")
    }
    catch (ex: Exception){
        return null
    }
    if(s == null) return null
    if(s.size < 4) return null
    //Logm.aa(s[3])

    if (s[2] != "devsend") return null

    val numObj = s[1]                             // тут можно найти нужный объект

    val topic1 = s[3]
    var topic2 = ""
    if (s.size > 4) topic2 = s[4]

    Logm.aa("t1:$topic1")
    val map : MutableMap<String, String> = mutableMapOf()

    if (topic1 == "status") {
        if (topic2 == "cp") {
            val map1 = multiTopic(mes) ?: return null
            map.putAll(map1)
        }
        else if (topic2 == "sout"){
            map.put(topic2, mes)
        }
    }
    else if (topic1 == "param") {
        val map1 = multiTopic(mes) ?: return null
        map.putAll(map1)
    }
    else if (topic1 == "config") {

    }
    return map
}

//  <92><03><E2><B7><01>2D<00><1C>a1021/1021/devsend/status/cp<00><07>PartStat=01&ZoneStat=02&ZoneAlarm=00
fun RecPublish(b: ByteArray, len_src: Int) : Int {
    if(len_src < 10) return 0

    Logm.aa(b, len_src);
    val bb = b[1].toInt()
    var lenPublish = bb and 0xff
    var pLenTopic = 2                        // указатель на длину темы
    if (lenPublish and 0x80 != 0) {
        var m : Int = b[2].toInt() and 0xff
        if (m == 0 || m > 3) m = 1
        lenPublish += (m - 1) * 128   ; Logm.aa("len pub: $lenPublish")
        pLenTopic = 3
    }
    //Logm.aa("len pub: $lenPublish")
    if (len_src < lenPublish + 2) return 0

    val lenTopic : Int = (b[pLenTopic].toInt() shl 8) + b[pLenTopic + 1].toInt()        // длина темы (строки)
    val p_pId : Int = lenTopic + pLenTopic + 2                                  // указатель на идентификатор пакета
    val packId : Int = (b[p_pId].toInt() shl 8) + b[p_pId + 1].toInt()                  // идентификатор (нужно ответить ACK)
    val p_Mes = p_pId + 2                                                // указатель на сообщение
    val p_Topic = pLenTopic + 2                                          // указатель на тему
    var i = 0

    val b_topic = ByteArray(lenTopic)
    i = 0
    while (i < lenTopic) {
        b_topic[i] = b[i + p_Topic]
        i++
    }
    Logm.aa("topic:")
    Logm.aa(b_topic, lenTopic)

    val lenMes = lenPublish - p_Mes + 2
    val b_message = ByteArray(lenMes)
    i = 0
    while (i < lenMes) {
        b_message[i] = b[i + p_Mes]
        i++
    }
    Logm.aa("mes:")
    Logm.aa(b_message, lenMes)

    val topic = Str.byte2str(b_topic, lenTopic)
    val message = Str.byte2str(b_message, lenMes)

    val map = checkTopic(topic, message) ?: return packId

    Logm.aa("mqtt map: ")
    for((key, value) in map){
        Logm.aa("key=$key");
        Logm.aa("value=$value");
    }

    return packId
}