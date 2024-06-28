package app.dayacore.kds.data

import app.dayacore.data.local.model.kds.KdsSettingsData

/* preference */
fun KdsSettingsData.toDto() = KdsSettingsDto(
    viewMode = viewMode,
    visitPurposeId = visitPurposeId,
    timeFirstWarning = timeFirstWarning,
    timeSecondWarning = timeSecondWarning,
    stationsId = stationsId,
    printingStationId = printingStationId
)