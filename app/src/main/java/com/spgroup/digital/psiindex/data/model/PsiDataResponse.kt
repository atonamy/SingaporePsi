package com.spgroup.digital.psiindex.data.model

import com.squareup.moshi.Json
import org.threeten.bp.LocalDateTime

/*
    Can add additional fields from api response.
    It's cut off for simplicity
 */

data class PsiDataResponse (
    @Json(name = "items") val singapore: List<Singapore>
) {
    data class Regions (
        @Json(name = "west") val west: Int,
        @Json(name = "east") val east: Int,
        @Json(name = "central") val central: Int,
        @Json(name = "south") val south: Int,
        @Json(name = "north") val north: Int
    )

    data class Data (
        @Json(name = "psi_twenty_four_hourly") val psi: Regions
    )

    data class Singapore (
        @Json(name = "update_timestamp") val lastUpdate: LocalDateTime,
        @Json(name = "readings") val data: Data
    )


}