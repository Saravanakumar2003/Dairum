package com.dairum.data.repository

import com.dairum.data.local.dao.LocationNicknameDao
import com.dairum.data.local.entity.LocationNicknameEntity
import com.dairum.domain.model.Location
import com.dairum.domain.model.LocationNickname
import com.dairum.domain.model.nicknameKey
import com.dairum.domain.repository.LocationNicknameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationNicknameRepositoryImpl @Inject constructor(
    private val dao: LocationNicknameDao
) : LocationNicknameRepository {

    override fun getAllFlow(): Flow<List<LocationNickname>> {
        return dao.getAllFlow().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getAll(): List<LocationNickname> {
        return dao.getAll().map { it.toDomain() }
    }

    override suspend fun setNickname(location: Location, nickname: String) {
        if (nickname.isBlank()) {
            removeNickname(location)
            return
        }
        dao.upsert(
            LocationNicknameEntity(
                locationKey = location.nicknameKey(),
                nickname = nickname.trim(),
                latitude = location.latitude,
                longitude = location.longitude,
                placeName = location.placeName,
                address = location.address
            )
        )
    }

    override suspend fun removeNickname(location: Location) {
        dao.deleteByKey(location.nicknameKey())
    }

    private fun LocationNicknameEntity.toDomain(): LocationNickname {
        return LocationNickname(
            id = id,
            locationKey = locationKey,
            nickname = nickname,
            latitude = latitude,
            longitude = longitude,
            placeName = placeName,
            address = address
        )
    }
}
