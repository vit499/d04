package ru.vit499.d04.http

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import ru.vit499.d04.ui.misc.Account
import ru.vit499.d04.util.Logm
import ru.vit499.d04.util.Str
import java.io.*
import java.lang.Exception
import java.net.InetAddress
import java.net.Socket

class HttpCor() {

    val SIZEBUF: Int = 10000
    val server = Account.accServ
    //val strPort = Account.accPort
    val port: Int = 80  // strPort.toInt()
    val user = Account.accUser
    val pass = Account.accPass

    var isOpen: Boolean = false
    var socket: Socket? = null
    var outputStreamWriter: OutputStreamWriter? = null
    //var inputStreamReader: InputStreamReader? = null
    var dataInputStream : DataInputStream? = null
    var inputStream: InputStream? = null
//    var rbuf1: CharArray = CharArray(10000)
//    var rbuf2: CharArray = CharArray(10000)
    var rbuf1: ByteArray = ByteArray(10000)
    var rbuf2: ByteArray = ByteArray(10000)
    var rcnt1: Int = 0
    var rcnt2: Int = 0

    fun send(
        strSend: String
    ) : Boolean {
        isOpen = false
        var resStr : String = "error"
        try{
            socket = Socket(InetAddress.getByName(server), port)
            Logm.aa("connected")
            socket?.setSoTimeout(2000)
            val outputStream : OutputStream? = socket?.getOutputStream() ?: return false
            val inputStream : InputStream? = socket?.getInputStream() ?: return false
            outputStreamWriter = OutputStreamWriter(outputStream!!)
           // inputStreamReader = InputStreamReader(inputStream!!)
            dataInputStream = DataInputStream(inputStream!!)
            //inputStream = socket?.getInputStream()

            rcnt2 = 0
            isOpen = true

            outputStreamWriter?.write(strSend)
            outputStreamWriter?.flush()
            Logm.aa("sended")

        }
        catch(ex: Exception) {
            Logm.aa("open exc: ${ex.toString()}")
        }
        return(isOpen)
    }

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
            Logm.aa(s)
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
        Logm.aa(r)
        if (!Str.checkHttpOk(buf, len)) {
            if (len > 100) len = 100
            val r2 = Str.byte2str(buf, 0, len)
            if (r2 == null || r2.length < 2) return null
            Logm.aa(r2)
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
            Logm.aa("res: $s")
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
        Logm.aa("ht rec: $r2")
        val a = r2.indexOf("111", 0)
        if(a != -1) {
            s = "ok"
        }
        else {
            s = "error"
        }
        return s
    }

    suspend fun rec (timeout: Long, wa : Int) : String {
        val t = timeout * 1000
        return withContext(Dispatchers.Default) {
            var resStr: String = "error"
            withTimeout(t) {
                while (true) {
                    if (!isOpen || !isActive) {
                        Close()
                        break;
                    }
                    try {
                        rcnt1 = dataInputStream?.read(rbuf1) ?: 0
                        if(rcnt1 > 0) {
                            for (j in 0 until rcnt1) {
                                if (rcnt2 < SIZEBUF) {
                                    rbuf2[rcnt2++] = rbuf1[j]
                                }
                            }
                            var s : String? = null
                            if(wa == 1) {
                                s = recState(rbuf2, rcnt2)
                            }
                            else if(wa == 2) {
                                s = recEvent(rbuf2, rcnt2)
                            }
                            else {
                                s = rec1111(rbuf2, rcnt2)
                            }
                            if (s != null) {
                                resStr = s
                                Close()
                                break
                            }
                        }
                    } catch (ex: Exception) {
                        //Logm.aa("rec exc: ${ex.toString()}")
                    }
                }
            }
            resStr
        }
    }

    fun Close(){
        try {
            outputStreamWriter?.close()
            dataInputStream?.close()
            socket?.shutdownOutput()
            socket?.shutdownInput()
            socket?.close()
        }
        catch(ex: Exception){
            Logm.aa("close exc: ${ex.toString()}")
        }
        isOpen = false
    }

    suspend fun reqStat(strSend : String, wa: Int, timeout: Long) : String {
        var s = ""
        return withContext(Dispatchers.Default) {
            val b = send(strSend)
            if (b) {
                s = rec(timeout, wa)
            }
            Close()
            s
        }
    }

}