package ru.vit499.d04.objstate

import ru.vit499.d04.database.Obj
import ru.vit499.d04.ui.outputs.OutItem
import ru.vit499.d04.util.Logm
import ru.vit499.d04.util.Str

class OutState(var obj: Obj) {

    val NUMBER_OUT = 32

    var functOut = IntArray(NUMBER_OUT)
    var ftOut = IntArray(NUMBER_OUT)
    var statOut = IntArray(NUMBER_OUT)

    init {
        fillOutState()

    }

    fun fillOutState(){
        fillSout(obj)
        fillFout(obj)
        fillFtout(obj)
    }

    fun fillSout(obj: Obj){
        val b = Str.Str2Bin(obj.sout)
        for (i in 0 until b.len) {
            val byte = b.buf[i].toInt()
            var c : Int = 0
            for(j in 0 until 8){
                val o = i * 8 + j
                if (o >= NUMBER_OUT) break
                val bit = byte and ((1 shl (7-j)))
                if(bit != 0) c = c + (1 shl j)
            }
            statOut[i] = c
        }
    }
    fun fillFout(obj: Obj){
        val b = Str.Str2Bin(obj.fout)
        for (i in 0 until b.len) {
            if (i >= NUMBER_OUT) break
            functOut[i] = b.buf[i].toInt()
        }
    }
    fun fillFtout(obj: Obj){
        val b = Str.Str2Bin(obj.ftout)
        for (i in 0 until b.len) {
            if (i >= NUMBER_OUT) break
            ftOut[i] = b.buf[i].toInt()
        }
    }


    //
    fun getOutStatList(): ArrayList<OutItem> {


        val listStat = ArrayList<OutItem>()

        //Logm.aa("nout:"  + " " + np.toString())
        for (p in 0 until 4) {

            val out = OutItem(p, functOut[p], statOut[p], ftOut[p] )

            listStat.add(out)

        }
        return listStat

    }
}