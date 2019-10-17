package ru.vit499.d04.objstate

class ObjPartZone(id1: Int, objName: String, parts: ArrayList<Part>) {

    var id: Int = 0
    var num: String = ""
    var part = ArrayList<Part>()

    init {
        id = id1
        num = objName
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