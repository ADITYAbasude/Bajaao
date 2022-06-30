package com.example.bajaao.constants

import java.util.concurrent.TimeUnit

class ConstantsFunc {
    fun convertTime(duration: Long): String {
        val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds = TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) -
                minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)

        return String.format("%02d:%02d", minutes, seconds)
    }
}