package app.dayacore.kds.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KdsSettingsDto(
    @SerialName("view_mode")
    var viewMode: String? = null,
    @SerialName("visit_purpose_id")
    var visitPurposeId: List<String>? = null,
    @SerialName("time_first_warning")
    var timeFirstWarning: Long? = null,
    @SerialName("time_second_warning")
    var timeSecondWarning: Long? = null,
    @SerialName("stations_id")
    var stationsId: List<String>? = null,
    @SerialName("printing_station_id")
    var printingStationId: List<String>? = null,
)