package com.example.util

import org.threeten.bp.DateTimeException
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.Period
import org.threeten.bp.format.DateTimeFormatter


private const val BD_FORMAT_INPUT = "dd-MM-yyyy"

object DateUtil {

    private val ofPattern: DateTimeFormatter = DateTimeFormatter.ofPattern(BD_FORMAT_INPUT)
    private val now get() = LocalDate.now().atTime(LocalTime.now())
    private val startOfDay = now.toLocalDate().atStartOfDay().toLocalDate()


    suspend fun calcAge(birthday: String): String {
        return try {
            val birthdayDate = try {
                LocalDate.parse(birthday, ofPattern)
            } catch (de: DateTimeException) {
                startOfDay
            }
            val p: Period = Period.between(birthdayDate, now.toLocalDate())
            "${p.years} years, ${p.months} months, ${p.days} days,\n ${now.hour}h, ${now.minute}m, ${now.second}s."
                    .also {
                        println(it)
                    }

        } catch (e: Exception) {
            e.printStackTrace()
            e.message.toString()
        }
    }

}