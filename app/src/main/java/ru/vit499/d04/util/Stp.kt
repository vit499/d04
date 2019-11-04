package ru.vit499.d04.util

class Stp {

    companion object {

        var r : Boolean = false
        var fbId : Int = 1
        var numObj : String? = null
        var mes: String? = null

        fun en(b : Boolean){
            r  = b
        }
        fun getEn(): Boolean {
            return r
        }
        fun getId(numObj: String) : Int {
            val n = numObj.toInt()
            return n
        }
        fun setFbId (num: String?, m: String?) : Int {
            numObj = num
            mes = m
            var n : Int = 0
            if(num != null) n = num.toInt()

            return n
        }
        fun clrFbId(){
            numObj = null
            mes = null
        }

        fun getPm() : Boolean {
            val b = Filem.getPm()
            return b
        }
        fun setPm(b: Boolean){
            Filem.setPm(b)
        }
    }
}