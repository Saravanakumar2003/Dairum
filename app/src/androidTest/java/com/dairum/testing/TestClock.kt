package com.dairum.testing

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset

/** A clock the test moves by hand, so updatedAt/deletedAt assertions are exact. */
class TestClock(startMillis: Long = 1_000L) : Clock() {
    @Volatile
    var millis: Long = startMillis

    fun advanceTo(newMillis: Long) {
        check(newMillis > millis) { "clock must move forward" }
        millis = newMillis
    }

    override fun getZone(): ZoneId = ZoneOffset.UTC
    override fun withZone(zone: ZoneId): Clock = this
    override fun instant(): Instant = Instant.ofEpochMilli(millis)
    override fun millis(): Long = millis
}
