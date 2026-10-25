package com.dairum.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import javax.inject.Singleton

/**
 * The clock repositories use to stamp `updatedAt`/`deletedAt`. Injected so tests can freeze and
 * advance time. UTC because only [Clock.millis] (epoch milliseconds) is ever read.
 */
@Module
@InstallIn(SingletonComponent::class)
object ClockModule {

    @Provides
    @Singleton
    fun provideClock(): Clock = Clock.systemUTC()
}
