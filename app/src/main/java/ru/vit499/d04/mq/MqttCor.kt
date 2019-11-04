package ru.vit499.d04.mq

import android.os.CountDownTimer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import ru.vit499.d04.ui.misc.Account
import ru.vit499.d04.util.Buf
import ru.vit499.d04.util.Logm
import java.io.*
import java.net.InetAddress
import java.net.Socket

class MqttCor {

    companion object {
        private const val SIZEBUF: Int = 100000
        private const val ONE_SEC : Long = 1000
        //val port: Int = 80  // strPort.toInt()
        const val tmax: Int = 10
    }

    private var isOpen: Boolean = false
    private var socket: Socket? = null
    //private var outputStreamWriter: OutputStreamWriter? = null
    //var inputStreamReader: InputStreamReader? = null
    private var dataOutputStream: DataOutputStream? = null
    private var dataInputStream : DataInputStream? = null
    //private var inputStream: InputStream? = null
    private var rbuf1: ByteArray = ByteArray(SIZEBUF)
    private var rbuf2: ByteArray = ByteArray(SIZEBUF)
    private var rcnt1: Int = 0
    private var rcnt2: Int = 0

    init {
//        timer = object : CountDownTimer(tmax.toLong()*ONE_SEC, ONE_SEC) {
//            override fun onTick(mSec: Long) {
//                val t = (mSec / ONE_SEC)
//                //Logm.aa("timer=$t")
//            }
//
//            override fun onFinish() {
//                Logm.aa("timer end")
//                Close()
//            }
//        }
        //timer.start()
    }

    private fun sendAck(packId: Int) {
        val b = FillAck(packId)
        send(b.buf, b.len)
    }

    private fun send(b: ByteArray, len: Int) : Boolean {
        var isSend = false
        if(!isOpen) return false
        if(socket == null) return false
        if(dataOutputStream == null) return false
        //rcnt2 = 0
        Logm.aa("mq send")
        Logm.aa(b, len)
        try {
            dataOutputStream?.write(b, 0, len)
            Logm.aa("sended")
            isSend = true
        }
        catch(ex: Exception) {
            Logm.aa("send exc: ${ex.toString()}")
            Logm.ex(ex)
        }
        catch(ex: IOException){
            Logm.aa("send exc: ${ex.toString()}")
            Logm.ex(ex)
        }
        return isSend
    }

    private fun connect(
        b: ByteArray, len: Int
    ) : Boolean {
        isOpen = false
        //var resStr : String = "error"
        val server = Account.accServ
        val strPort = Account.accPort
        val port : Int = strPort.toInt()
        Logm.aa("server: $server : $port")
        try{
            socket = Socket(InetAddress.getByName(server), port)
            Logm.aa("connected")
            socket?.setSoTimeout(2000)
            val outputStream : OutputStream? = socket?.getOutputStream() ?: return false
            val inputStream : InputStream? = socket?.getInputStream() ?: return false
            dataOutputStream = DataOutputStream(outputStream!!)
            dataInputStream = DataInputStream(inputStream!!)
            isOpen = true

            send(b, len)
        }
        catch(ex: Exception) {
            Logm.aa("open exc: ${ex.toString()}")
            Logm.ex(ex)
        }
        catch(ex: IOException){
            Logm.aa("open exc: ${ex.toString()}")
            Logm.ex(ex)
        }
        return(isOpen)
    }

    suspend fun rec (wa: Int, updCallback: (Map<String, String>) -> Unit) : String {
        return withContext(Dispatchers.IO) {
            var resStr: String = "error"
            rcnt2 = 0
            while (true) {
                if (!isActive) {
                    Logm.aa("mqtt not Active")
                    Close()
                    break;
                }
                if(socket == null || dataInputStream == null) {
                    Logm.aa("mqtt end socket null")
                    Close()
                    break;
                }
                try {
                    if(socket!!.isClosed || !socket!!.isConnected) {
                        Logm.aa("mqtt end socket closed")
                        Close()
                        break;
                    }
                    rcnt1 = dataInputStream?.read(rbuf1) ?: 0
                    if(rcnt1 > 0) {
                        for (j in 0 until rcnt1) {
                            if (rcnt2 < SIZEBUF) {
                                rbuf2[rcnt2++] = rbuf1[j]
                            }
                        }
                        var s : String? = null

                        //Logm.aa(rbuf2, rcnt2)
                        if(wa == 1){
                            s = RecConnect(rbuf2, rcnt2)
                            if(s != null) {
                                resStr = s
                                break
                            }
                        }
                        else if(wa == 2) {
                            s = RecSub(rbuf2, rcnt2)
                            if(s != null) {
                                resStr = s
                                break
                            }
                        }
                        else if(wa == 3) {
                            //val packId = RecPublish(rbuf2, rcnt2, updCallback)
                            val packId = GetPackId(rbuf2, rcnt2)
                            val map = GetMap(rbuf2, rcnt2)
                            if(map != null) updCallback(map)
                            if(packId != 0) {
                                // send Ack
                                Logm.aa("packId=$packId")
                                sendAck(packId)
                            }
                            rcnt2 = 0
                            //if(map != null) updCallback(map)
                        }

                    }
                } catch (ex: IOException) {
                    //Logm.aa("rec IOexc: ${ex.toString()}")
                } catch (ex: Exception){
                    //Logm.aa("rec exc: ${ex.toString()}")
                }
            }
            resStr
        }
    }

    fun Close(){
        Logm.aa("mqtt close")
        try {
            dataOutputStream?.close()
            dataInputStream?.close()
            socket?.shutdownOutput()
            socket?.shutdownInput()
            socket?.close()
            socket = null
        }
        catch(ex: Exception){
            //Logm.aa("close exc: ${ex.toString()}")
        }
        isOpen = false
    }

    suspend fun subMqtt(
        numObj: String,
        updCallback: (Map<String, String>) -> Unit ) {

        val strConn = FillConnectMqtt()
        val strSub = FillSubMqtt(numObj)
        var s : String? = null
        return withContext(Dispatchers.IO) {

            val sendConnectMqtt = connect(strConn.buf, strConn.len)  // connect Mqtt
            if(!sendConnectMqtt) return@withContext
            s = rec(1, updCallback)
            if(s.equals("error")) return@withContext

            val sendSubMqtt = send(strSub.buf, strSub.len)
            if(!sendSubMqtt) return@withContext
            s = rec(2, updCallback)
            if(s.equals("error")) return@withContext

            Logm.aa("subscribed")
            s = rec(3, updCallback)   // forever
        }
    }
}