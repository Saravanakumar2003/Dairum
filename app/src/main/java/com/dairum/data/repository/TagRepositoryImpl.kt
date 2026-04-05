package com.dairum.data.repository

import com.dairum.data.local.dao.TagDao
import com.dairum.data.local.entity.TagEntity
import com.dairum.domain.model.Tag
import com.dairum.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
    private val tagDao: TagDao
) : TagRepository {

    override suspend fun create(tag: Tag): Long {
        return tagDao.insert(tag.toEntity())
    }

    override suspend fun createOrGet(name: String): Tag {
        val existing = tagDao.getByName(name)
        if (existing != null) {
            return existing.toDomain()
        }
        val id = tagDao.insert(TagEntity(name = name))
        return Tag(id = id, name = name)
    }

    override suspend fun update(tag: Tag) {
        tagDao.update(tag.toEntity())
    }

    override suspend fun delete(id: Long) {
        tagDao.deleteById(id)
    }

    override suspend fun getById(id: Long): Tag? {
        return tagDao.getById(id)?.toDomain()
    }

    override suspend fun getByName(name: String): Tag? {
        return tagDao.getByName(name)?.toDomain()
    }

    override fun getAllFlow(): Flow<List<Tag>> {
        return tagDao.getAllFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getAllWithCountFlow(): Flow<List<Tag>> {
        return tagDao.getAllWithCountFlow().map { list ->
            list.map { 
                Tag(
                    id = it.tag.id,
                    name = it.tag.name,
                    color = it.tag.color,
                    usageCount = it.entryCount
                )
            }
        }
    }

    override suspend fun search(query: String, limit: Int): List<Tag> {
        return tagDao.search(query, limit).map { it.toDomain() }
    }

    override suspend fun getTopTags(limit: Int): List<Tag> {
        return tagDao.getTopTags(limit).map { 
            Tag(
                id = it.tag.id,
                name = it.tag.name,
                color = it.tag.color,
                usageCount = it.entryCount
            )
        }
    }

    override suspend fun getTagsForEntry(entryId: Long): List<Tag> {
        return tagDao.getTagsForEntry(entryId).map { it.toDomain() }
    }

    private fun TagEntity.toDomain(): Tag {
        return Tag(
            id = id,
            name = name,
            color = color,
            usageCount = usageCount
        )
    }

    private fun Tag.toEntity(): TagEntity {
        return TagEntity(
            id = id,
            name = name,
            color = color,
            usageCount = usageCount
        )
    }
}
