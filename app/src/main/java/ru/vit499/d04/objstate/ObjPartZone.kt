package ru.vit499.d04.objstate

class ObjPartZone(parts: ArrayList<Part>) {

    // object:
    //   part1: { zone1,zone2... }
    //   part2: { zone5,zone6... }
    //   ....
    // класс состоит из списка разделов, разделы состоят из списка зон

    var part = ArrayList<Part>()

    init {
        //id = id1
        //num = objName
        copyArr(parts)
    }

    fun copyArr (p: ArrayList<Part>) {
        var k : Int = p.size
        part.clear()
        for(i in 0 until k){
            part.add(p.get(i))
        }
    }
}