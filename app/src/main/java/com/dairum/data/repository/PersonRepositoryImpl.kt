package com.dairum.data.repository

import com.dairum.data.local.dao.PersonDao
import com.dairum.data.local.entity.PersonEntity
import com.dairum.domain.model.ImportantDate
import com.dairum.domain.model.Person
import com.dairum.domain.repository.PersonRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Clock
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val personDao: PersonDao,
    private val moshi: Moshi,
    private val clock: Clock
) : PersonRepository {

    private val importantDatesAdapter = moshi.adapter<List<ImportantDateJson>>(
        Types.newParameterizedType(List::class.java, ImportantDateJson::class.java)
    )

    override suspend fun create(person: Person): Long {
        val entity = person.toEntity()
        return personDao.insert(if (entity.createdAt == 0L) entity.copy(createdAt = clock.millis()) else entity)
    }

    override suspend fun update(person: Person) {
        val entity = person.toEntity()
        // createdAt never changes. The edit dialog builds a fresh Person(id = ...) without it (0),
        // so fall back to the stored value instead of letting @Update reset it to "now".
        val createdAt = if (entity.createdAt > 0L) entity.createdAt
        else personDao.getById(person.id)?.createdAt ?: clock.millis()
        personDao.update(entity.copy(createdAt = createdAt))
    }

    override suspend fun delete(id: Long) {
        personDao.deleteById(id)
    }

    override suspend fun getById(id: Long): Person? {
        return personDao.getById(id)?.toDomain()
    }

    override fun getAllFlow(): Flow<List<Person>> {
        return personDao.getAllFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getAllWithCountFlow(): Flow<List<Person>> {
        return personDao.getAllWithCountFlow().map { list ->
            list.map { 
                Person(
                    id = it.person.id,
                    name = it.person.name,
                    relationship = it.person.relationship,
                    photoUri = it.person.photoUri,
                    notes = it.person.notes,
                    email = it.person.email,
                    phone = it.person.phone,
                    birthday = it.person.birthday,
                    importantDates = parseImportantDates(it.person.importantDates),
                    isFavorite = it.person.isFavorite,
                    mentionCount = it.entryCount,
                    createdAt = it.person.createdAt
                )
            }
        }
    }

    override suspend fun search(query: String, limit: Int): List<Person> {
        return personDao.search(query, limit).map { it.toDomain() }
    }

    override suspend fun getTopPeople(limit: Int): List<Person> {
        return personDao.getTopPeople(limit).map { 
            Person(
                id = it.person.id,
                name = it.person.name,
                relationship = it.person.relationship,
                photoUri = it.person.photoUri,
                notes = it.person.notes,
                email = it.person.email,
                phone = it.person.phone,
                birthday = it.person.birthday,
                importantDates = parseImportantDates(it.person.importantDates),
                isFavorite = it.person.isFavorite,
                mentionCount = it.entryCount,
                createdAt = it.person.createdAt
            )
        }
    }

    override suspend fun getPeopleForEntry(entryId: Long): List<Person> {
        return personDao.getPeopleForEntry(entryId).map { it.toDomain() }
    }

    override suspend fun toggleFavorite(id: Long, isFavorite: Boolean) {
        personDao.updateFavorite(id, isFavorite)
    }

    private fun PersonEntity.toDomain(): Person {
        return Person(
            id = id,
            name = name,
            relationship = relationship,
            photoUri = photoUri,
            notes = notes,
            email = email,
            phone = phone,
            birthday = birthday,
            importantDates = parseImportantDates(importantDates),
            isFavorite = isFavorite,
            mentionCount = mentionCount,
            createdAt = createdAt
        )
    }

    private fun Person.toEntity(): PersonEntity {
        return PersonEntity(
            id = id,
            name = name,
            relationship = relationship,
            photoUri = photoUri,
            notes = notes,
            email = email,
            phone = phone,
            birthday = birthday,
            importantDates = serializeImportantDates(importantDates),
            isFavorite = isFavorite,
            mentionCount = mentionCount,
            createdAt = createdAt
        )
    }

    private fun parseImportantDates(json: String?): List<ImportantDate> {
        if (json.isNullOrBlank()) return emptyList()
        return try {
            importantDatesAdapter.fromJson(json)?.map { 
                ImportantDate(label = it.label, date = it.date) 
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun serializeImportantDates(dates: List<ImportantDate>): String? {
        if (dates.isEmpty()) return null
        return try {
            importantDatesAdapter.toJson(dates.map { ImportantDateJson(it.label, it.date) })
        } catch (e: Exception) {
            null
        }
    }
}

private data class ImportantDateJson(
    val label: String,
    val date: Long
)
