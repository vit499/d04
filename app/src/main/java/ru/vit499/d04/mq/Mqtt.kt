package ru.vit499.d04.mq

import ru.vit499.d04.ui.misc.Account
import ru.vit499.d04.util.Buf
import ru.vit499.d04.util.Logm
import java.io.*
import java.net.InetAddress
import java.net.Socket

class Mqtt {

    companion object {
        private const val SIZEBUF: Int = 100000
        private const val ONE_SEC : Long = 1000
        //val port: Int = 80  // strPort.toInt()
        const val tmax: Int = 10
    }
    private val server = Account.accServ
    private val strPort = Account.accPort
    private val port : Int = strPort.toInt()

    //private val timer: CountDownTimer

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


    private fun send(b: ByteArray, len: Int) : Boolean {
        var isSend = false
        if(!isOpen) return false
        if(socket == null) return false
        if(dataOutputStream == null) return false
        rcnt2 = 0
        Logm.aa("mq send")
        Logm.aa(b, len)
        try {
            dataOutputStream?.write(b, 0, len)
            Logm.aa("sended")
            isSend = true
        }
        catch(ex: Exception) {
            Logm.aa("send exc: ${ex.toString()}")
        }
        catch(ex: IOException){
            Logm.aa("send exc: ${ex.toString()}")
        }
        return isSend
    }

    private fun connect(
        b: ByteArray, len: Int, updCallback: (String) -> Unit
    ) : Boolean {
        isOpen = false
        //var resStr : String = "error"
        try{
            socket = Socket(InetAddress.getByName(server), port)
            Logm.aa("connected")
            socket?.setSoTimeout(2000)
            val outputStream : OutputStream? = socket?.getOutputStream() ?: return false
            val inputStream : InputStream? = socket?.getInputStream() ?: return false
           // outputStreamWriter = OutputStreamWriter(outputStream!!)
            dataOutputStream = DataOutputStream(outputStream!!)
            dataInputStream = DataInputStream(inputStream!!)
            isOpen = true

            val thread = Thread(){
                var resStr: String = "error"
                while (true) {

                    if(socket == null || dataInputStream == null) {
                        Logm.aa("rec end socket null")
                        Close()
                        break;
                    }
                    try {
                        if(socket!!.isClosed || !socket!!.isConnected) {
                            Logm.aa("rec end socket closed")
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

                            Logm.aa(rbuf2, rcnt2)
                            RecMqtt(rbuf2, rcnt2)
                            updCallback("ab")
                        }
                    } catch (ex: IOException) {
                        Logm.aa("rec IOexc: ${ex.toString()}")
                    } catch (ex: Exception){
                        Logm.aa("rec exc: ${ex.toString()}")
                    }
                }
            }
            thread.start()

            send(b, len)
        }
        catch(ex: Exception) {
            Logm.aa("open exc: ${ex.toString()}")
        }
        catch(ex: IOException){
            Logm.aa("open exc: ${ex.toString()}")
        }
        return(isOpen)
    }



    fun Close(){
        Logm.aa("http close")
        try {
            dataOutputStream?.close()
            dataInputStream?.close()
            socket?.shutdownOutput()
            socket?.shutdownInput()
            socket?.close()
        }
        catch(ex: Exception){
            //Logm.aa("close exc: ${ex.toString()}")
        }
        isOpen = false
    }

    fun mqttStart(
        numObj: String,
        updCallback: (String) -> Unit ){

        val strConn = FillConnectMqtt()
        val strSub = FillSubMqtt(numObj)
        Logm.aa(strConn)
        Logm.aa(strSub)

        val thread = Thread() {
            connect(strConn.buf, strConn.len, updCallback)
            send(strSub.buf, strSub.len)
            //Send(strSend, resultReq)
        }
        thread.start()
    }


}