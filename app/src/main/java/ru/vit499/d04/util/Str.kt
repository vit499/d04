package ru.vit499.d04.util

class Str {

    companion object {

        fun bcd2char1(b: Byte): Char {
            val c: Char
            var k: Int = b.toInt()
            k = (k shr 4) and 0x0f
            if (k < 10)
                k = k + 0x30
            else
                k = k + 0x37
            c = k.toChar()
            return c
        }

        fun bcd2char0(b: Byte): Char {
            val c: Char
            var k: Int = b.toInt()
            k = k and 0x0f
            if (k < 10)
                k = k + 0x30
            else
                k = k + 0x37
            c = k.toChar()
            return c
        }

        fun bcd2char1(b: Int): Byte {
            val c: Byte
            var k: Int
            k = b shr 4 and 0x0f
            if (k < 10)
                k = k + 0x30
            else
                k = k + 0x37
            c = k.toByte()
            return c
        }

        fun bcd2char0(b: Int): Byte {
            val c: Byte
            var k: Int
            k = b and 0x0f
            if (k < 10)
                k = k + 0x30
            else
                k = k + 0x37
            c = k.toByte()
            return c
        }

        // 0x02 0xb7 -> <02><B7>
        fun hex2str(src: ByteArray, len: Int): String {
            val sb = StringBuilder()
            var b: Byte
            var c: Char
            var i: Int

            i = 0
            while (i < len) {
                b = src[i]
                if (b < 0x20 || b > 0x7e) {
                    sb.append('<')
                    // c = bcd2char1(b);
                    sb.append(bcd2char1(b))
                    // c = bcd2char0(b);
                    sb.append(bcd2char0(b))
                    sb.append('>')
                } else {
                    c = b.toChar()
                    sb.append(c)
                }
                i++
            }
            return sb.toString()
        }

        fun hex2str(src: ByteArray, off: Int, len: Int): String {
            val sb = StringBuilder()
            var b: Byte
            var c: Char
            var i: Int

            i = off
            while (i < len) {
                b = src[i]
                if (b < 0x20 || b > 0x7e) {
                    sb.append('<')
                    // c = bcd2char1(b);
                    sb.append(bcd2char1(b))
                    // c = bcd2char0(b);
                    sb.append(bcd2char0(b))
                    sb.append('>')
                } else {
                    c = b.toChar()
                    sb.append(c)
                }
                i++
            }
            return sb.toString()
        }

        fun byte2str(src: ByteArray, len: Int): String {
            return if (len == 0) "" else String(src, 0, len)
        }

        fun byte2str(src: ByteArray, a: Int, b: Int): String {
            //if(len == 0) return("");
            val len = b - a
            return String(src, a, len)
        }

        fun ByteToStr(src: ByteArray, len: Int): String {
            return String(src, 0, len)
        }

        // "34" -> 0x33 0x34
        fun StrToByte(s: String): Buf {
            val b = Buf()

            //b.len = s.getBytes().length;
            val b1 = s.toByteArray()
            val len = b1.size

            for (i in 0 until len) b.Add(b1[i].toInt()) // b.buf[i] = b1[i];
            return b
        }

        fun char2bcd(bt: Byte): Byte {
            val c: Byte
            val b = bt.toInt()

            if (b >= 0x30 && b <= 0x39)
                c = (b - 0x30).toByte()
            else if (b >= 0x41 && b <= 0x46)
                c = (b - 0x37).toByte()
            else
                c = 0x7f
            return c
        }

        // { 0x34 0x46 0x33 } -> 0x04 0x0F 0x03
        fun Str2Hex(src: ByteArray, StartIndex: Int, len_src: Int): Buf {
            val b = Buf()
            var b1: Int
            var b2: Int
            var b3: Int
            var i: Int
            val len = 0

            i = StartIndex
            while (i < StartIndex + len_src) {
                b1 = char2bcd(src[i]).toInt()
                if (b1 == 0x7f) {
                    b.len = 0
                    return b
                }
                b1 = b1 shl 4
                b2 = char2bcd(src[i + 1]).toInt()
                if (b2 == 0x7f) {
                    b.len = 0
                    return b
                }
                b3 = b1 + b2
                b.Add(b3)
                i = i + 2
            }
            return b
        }

        fun indexof(buf: ByteArray, offset: Int, cbyte: Byte, num: Int, max: Int): Int {
            var pos: Int
            var n = 0
            var ind = -1

            pos = 0
            while (pos < max) {
                if (buf[pos + offset] == 0.toByte()) break
                if (buf[pos + offset] == cbyte) n++
                if (n == num) {
                    ind = pos + 1
                    break
                }
                pos++
            }
            return ind
        }

        fun strncmp3(s1: ByteArray, off1: Int, s2: ByteArray, len: Int): Int {
            for (i in 0 until len) {
                if (s1[off1 + i] != s2[i]) {
                    return i + 1
                }
            }
            return 0
        }

        // HTTP/1.1 200 OK
        fun checkHttpOk(buf: ByteArray, len: Int): Boolean {
            var p = -1
            val s1 = "HTTP"
            val s2 = "200"
            val http = s1.toByteArray()
            val httpOk = s2.toByteArray()

            if (strncmp3(buf, 0, http, 4) != 0) return false
            for (i in 0..11) {
                if (buf[i] == ' '.toByte()) {
                    p = i + 1
                    break
                }
            }
            if (p == -1) return false
            return if (strncmp3(buf, p, httpOk, 3) != 0) false else true
        }
    }
}