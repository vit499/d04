package ru.vit499.d04.util

import android.R
import android.content.res.ColorStateList

class Colors {

    companion object{
        val AquaCyan = 0xFF00FFFF.toInt()
        val LightCyan = 0xFFE0FFFF.toInt()
        val PaleTurquoise = 0xFFAFEEEE.toInt()
        val Aquamarine = 0xFF7FFFD4.toInt()
        val Turquoise = 0xFF40E0D0.toInt()
        val MediumTurquoise = 0xFF48D1CC.toInt()
        val DarkTurquoise = 0xFF00CED1.toInt()
        val CadetBlue = 0xFF5F9EA0.toInt()
        val SteelBlue = 0xFF4682B4.toInt()
        val LightSteelBlue = 0xFFB0C4DE.toInt()
        val PowderBlue = 0xFFB0E0E6.toInt()
        val LightBlue = 0xFFADD8E6.toInt()
        val SkyBlue = 0xFF87CEEB.toInt()
        val LightSkyBlue = 0xFF87CEFA.toInt()
        val DeepSkyBlue = 0xFF00BFFF.toInt()
        val DodgerBlue = 0xFF1E90FF.toInt()
        val CornflowerBlue = 0xFF6495ED.toInt()
        val MediumSlateBlue = 0xFF7B68EE.toInt()
        val RoyalBlue = 0xFF4169E1.toInt()
        val Blue = 0xFF0000FF.toInt()


        val GreenYellow = 0xFFADFF2F.toInt()
        val Chartreuse = 0xFF7FFF00.toInt()
        val LawnGreen = 0xFF7CFC00.toInt()
        val Lime = 0xFF00FF00.toInt()
        val LimeGreen = 0xFF32CD32.toInt()
        val PaleGreen = 0xFF98FB98.toInt()
        val LightGreen = 0xFF90EE90.toInt()
        val MediumSpringGreen = 0xFF00FA9A.toInt()
        val SpringGreen = 0xFF00FF7F.toInt()
        val MediumSeaGreen = 0xFF3CB371.toInt()
        val SeaGreen = 0xFF2E8B57.toInt()
        val ForestGreen = 0xFF228B22.toInt()
        val Green = 0xFF008000.toInt()
        val DarkGreen = 0xFF006400.toInt()
        val YellowGreen = 0xFF9ACD32.toInt()
        val OliveDrab = 0xFF6B8E23.toInt()
        val Olive = 0xFF808000.toInt()
        val DarkOliveGreen = 0xFF556B2F.toInt()
        val MediumAquamarine = 0xFF66CDAA.toInt()
        val DarkSeaGreen = 0xFF8FBC8F.toInt()
        val LightSeaGreen = 0xFF20B2AA.toInt()
        val DarkCyan = 0xFF008B8B.toInt()
        val Teal = 0xFF008080.toInt()


        val arrState = arrayOf(
            intArrayOf(R.attr.state_pressed), //1
            intArrayOf(R.attr.state_focused), //2
            intArrayOf(R.attr.state_focused, R.attr.state_pressed), //3
            intArrayOf()
        )

        val cSL1 = ColorStateList(
            arrState,
            intArrayOf(
                GreenYellow,
                LawnGreen,
                SkyBlue,
                Chartreuse
            )
        )
        val cSL2 = ColorStateList(
            arrState,
            intArrayOf(
                GreenYellow,
                LawnGreen,
                SkyBlue,
                LightSeaGreen
            )
        )
        val cSL3 = ColorStateList(
            arrState,
            intArrayOf(
                GreenYellow,
                LawnGreen,
                SkyBlue,
                Olive
            )
        )
    }
}