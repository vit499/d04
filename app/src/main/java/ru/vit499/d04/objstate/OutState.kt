package ru.vit499.d04.objstate

import ru.vit499.d04.database.Obj
import ru.vit499.d04.ui.outputs.OutItem
import ru.vit499.d04.util.Logm
import ru.vit499.d04.util.Str

class OutState(var obj: Obj) {

    val NUMBER_OUT = 32

    var functOut = IntArray(NUMBER_OUT)     // функция выхода
    var ftOut = IntArray(NUMBER_OUT)        // пороговая температура
    var statOut = IntArray(NUMBER_OUT)      // состояние выхода
    var temperFact = IntArray(NUMBER_OUT)   // фактическая температура
    var indexTemper = IntArray(NUMBER_OUT)  // датчик температуры, к которому привязан termoOut

    init {
        fillOutState()

    }

    fun fillOutState(){
        fillSout(obj)
        fillFout(obj)
        fillFtout(obj)
        fillTemperFact(obj)
        fillIndTemperOut(obj)
    }

    // sout="25000080" -> 0x25 0x00 0x00 0x80 -> 0010 0101 ...
    fun fillSout(obj: Obj){
        //Logm.aa("sout from obj: ${obj.sout}")
        for(i in 0 until NUMBER_OUT) statOut[i] = 0
        val b = Str.Str2Bin(obj.sout)
        for (i in 0 until b.len) {
            val byte = b.buf[i].toInt()
            for(j in 0 until 8){
                val o = i * 8 + j
                if (o >= NUMBER_OUT) break
                val bit = byte and (1 shl j)
                if(bit != 0) statOut[o] = 1
                //Logm.aa("o${o+1}=${statOut[o]}")
            }
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
    fun fillTemperFact(obj: Obj){
        val b = Str.Str2Bin(obj.reserv2)    // temper
        for (i in 0 until b.len) {
            if (i >= NUMBER_OUT) break
            temperFact[i] = b.buf[i].toInt()
        }
    }
    fun fillIndTemperOut(obj: Obj){
        val b = Str.Str2Bin(obj.reserv1)   // indtemp
        for (i in 0 until b.len) {
            if (i >= NUMBER_OUT) break
            indexTemper[i] = b.buf[i].toInt()
        }
    }


    //
    fun getOutStatList(): ArrayList<OutItem> {
        val listStat = ArrayList<OutItem>()

        var maxOut : Int = 0

        for(i in 0 until NUMBER_OUT) {
            if(functOut[i] != 0) maxOut = i + 1
        }
        if(maxOut < 4) maxOut = 4;
        //Logm.aa("nout:"  + " " + np.toString())
        for (p in 0 until maxOut) {
            var indexT = indexTemper[p]
            if(indexT >= NUMBER_OUT) indexT = 0
            val out = OutItem(p, functOut[p], statOut[p], ftOut[p], indexTemper[p], temperFact[indexT])

            listStat.add(out)

        }
        return listStat

    }
}