package com.wahidabd.remas.utils

import android.os.Build
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object DateTimeUtil {

    fun getDescriptiveMessageDateTime(msgDatetime: String, withTime: Boolean): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val currDate = LocalDateTime.now()
            val msgDate =
                LocalDate.parse(msgDatetime.substring(0, 10), dateTimeFormatter).atStartOfDay()
            val duration = Duration.between(msgDate, currDate)
            var msgDateStr: String

            when (duration.toDays()) {
                0L -> {
                    msgDateStr = "Hari ini"
                    if (withTime)
                        msgDateStr += ", " + msgDatetime.substring(11, 16)
                }
                1L -> {
                    msgDateStr = "Kemarin"
                    if (withTime)
                        msgDateStr += ", " + msgDatetime.substring(11, 16)
                }
                else -> {
                    val currYear = currDate.format(DateTimeFormatter.ISO_DATE).substring(0, 4)
                    val msgYear = msgDatetime.substring(0, 4)


                    // if the year is the same
                    msgDateStr = if (currYear == msgYear) {
                        val msgDateSplit = msgDate.format(
                            DateTimeFormatter.ofLocalizedDate(
                                FormatStyle.MEDIUM
                            )
                        ).split(", ")
                        msgDateSplit[0]
                    } else {
                        msgDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                    }
                }
            }
            return msgDateStr
        }
        return msgDatetime.substring(0, 10)
    }
}