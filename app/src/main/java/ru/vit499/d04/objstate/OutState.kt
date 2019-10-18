package ru.vit499.d04.objstate

import ru.vit499.d04.database.Obj
import ru.vit499.d04.ui.outputs.OutItem
import ru.vit499.d04.util.Logm
import ru.vit499.d04.util.Str

class OutState(var obj: Obj) {

    val NUMBER_OUT = 32

    internal var functOut = IntArray(NUMBER_OUT)
    internal var ftOut = IntArray(NUMBER_OUT)
    internal var statOut = IntArray(NUMBER_OUT)

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
            if (i >= NUMBER_OUT) break
            statOut[i] = b.buf[i].toInt()
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

            val out = OutItem(p+1, statOut[p], ftOut[p], functOut[p])

            listStat.add(out)

        }
        return listStat

    }
}