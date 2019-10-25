package com.spgroup.digital.psiindex.network.retrofit.adapters

import com.squareup.moshi.FromJson
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

object LocalDateTimeAdapter {
    @FromJson
    fun fromJson(date: String): LocalDateTime =
        LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[XXX][X]"))
}