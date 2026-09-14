package com.hop.rail.train

/**
 * Local-only train boarding context. Never transmitted over the mesh —
 * used only to derive room IDs (see [RoomResolver]) and to label the UI.
 */
data class TrainContext(
    val trainNumber: String?,
    val coachId: String?,
    val boardingTimestamp: Long
)
