package ru.vit499.d04.util

class Buf {

    var buf = ByteArray(0x1000)
    var len: Int = 0

    fun Clear() {
        len = 0
    }

    init {
        len = 0
    }

    fun Add(b: Int): Int {
        buf[len++] = b.toByte()
        return len
    }

    fun Cpy(s: String): Int {
        var i: Int
        val len_src = s.length
        val src = s.toByteArray()

        i = 0
        while (i < len_src) {
            buf[len++] = src[i]
            i++
        }
        return len_src
    }
}