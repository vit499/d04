package ru.vit499.d04.ui.main

class StatList(
    partOrZone: Int,
    ind1: Int,
    stat: Int,
    strStat: String,
    cmd: String,
    color: Int,
    descr1: String
) {

    private var name: String = ""
    private var descr: String = ""
    private var strStat: String = ""
    private var cmd: String = ""
    private var span: Int = 0
    private var strPart: Int = 0
    private var intColor: Int = 0
    private var ind: Int = 0

    init {
        ind = ind1
        descr = descr1
        if (partOrZone == 1) {  // part
            name = "Раздел " + (ind + 1).toString()
            intColor = color // intColorP[stat];
            this.strStat = strStat
            this.cmd = cmd
            span = 4
            strPart = 1
        } else {
            name = "Зона " + (ind + 1).toString() + "\r\n" + strStat
            intColor = color // intColorZ[stat];
            this.strStat = ""
            this.cmd = ""
            span = 1
            strPart = 0
        }

    }
    fun getName(): String {
        return name
    }

    fun getDesc(): String {
        return descr
    }

    fun getIntColor(): Int {
        return intColor
    }

    fun getStrStat(): String {
        return strStat
    }

    fun getCmd(): String {
        return cmd
    }

    fun getSpan(): Int {
        return span
    }

    fun getStrPart(): Int {
        return strPart
    }

    fun getInd(): Int {
        return ind
    }

    private val intColorP = intArrayOf(
        -0x9f00cd,
        -0xcccd,
        -0x1f4acd,
        -0x1f4acd,
        -0x1f4acd,
        -0x1f4acd,
        -0x1f4acd,
        -0x1f4acd,
        -0x1f4acd,
        -0x1f4acd,
        -0x1f4acd,
        -0x1f4acd,
        -0x1f4acd,
        -0x1f4acd
    )
    private val intColorZ = intArrayOf(
        -0x9f1000,
        -0xcccd,
        -0x33cccd,
        -0x9f1000,
        -0x9f1000,
        -0x9f1000,
        -0x9f1000,
        -0x9f1000,
        -0x9f1000,
        -0x9f1000,
        -0x9f1000,
        -0x9f1000,
        -0x9f1000
    )
}