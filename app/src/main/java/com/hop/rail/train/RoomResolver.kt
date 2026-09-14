package com.hop.rail.train

/**
 * Maps [TrainContext] to the plain-string room IDs carried in
 * `BitchatMessage.channel`. Deliberately not hashed or encrypted — anyone
 * physically on the train can read the coach label off the door, so there
 * is no secret to protect, and plain strings make debugging vastly easier.
 */
object RoomResolver {

    fun trainRoom(context: TrainContext): String? {
        val trainNumber = normalizeTrainNumber(context.trainNumber) ?: return null
        return "t/$trainNumber"
    }

    fun coachRoom(context: TrainContext): String? {
        val trainNumber = normalizeTrainNumber(context.trainNumber) ?: return null
        val coachId = normalizeCoachId(context.coachId) ?: return null
        return "t/$trainNumber/$coachId"
    }

    private fun normalizeTrainNumber(raw: String?): String? {
        val stripped = raw?.trim()?.replace(Regex("[^A-Za-z0-9]"), "")
        return stripped?.takeIf { it.isNotEmpty() }
    }

    private fun normalizeCoachId(raw: String?): String? {
        val trimmed = raw?.trim()?.uppercase()
        return trimmed?.takeIf { it.isNotEmpty() }
    }
}
