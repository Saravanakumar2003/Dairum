package com.dairum.data.repository

import com.dairum.data.local.dao.CategoryDao
import com.dairum.data.local.entity.CategoryEntity
import com.dairum.domain.model.Category
import com.dairum.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override suspend fun create(category: Category): Long {
        val maxOrder = categoryDao.getMaxSortOrder() ?: 0
        return categoryDao.insert(category.toEntity().copy(sortOrder = maxOrder + 1))
    }

    override suspend fun update(category: Category) {
        categoryDao.update(category.toEntity())
    }

    override suspend fun delete(id: Long) {
        categoryDao.deleteById(id)
    }

    override suspend fun getCategoryById(id: Long): Category? {
        return categoryDao.getById(id)?.toDomain()
    }

    override suspend fun getDefault(): Category? {
        return categoryDao.getDefault()?.toDomain()
    }

    override suspend fun setDefault(id: Long) {
        categoryDao.clearDefaultFlag()
        categoryDao.setAsDefault(id)
    }

    override fun getAllFlow(): Flow<List<Category>> {
        return categoryDao.getAllFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getAllWithCountFlow(): Flow<List<Category>> {
        return categoryDao.getAllWithCountFlow().map { list ->
            list.map { it.toDomainWithCount() }
        }
    }

    override suspend fun getTopCategories(limit: Int): List<Category> {
        return categoryDao.getTopCategories(limit).map { it.toDomainWithCount() }
    }

    override suspend fun updateSortOrder(id: Long, sortOrder: Int) {
        categoryDao.updateSortOrder(id, sortOrder)
    }

    private fun CategoryEntity.toDomain(): Category {
        return Category(
            id = id,
            name = name,
            color = color,
            icon = icon,
            sortOrder = sortOrder,
            isDefault = isDefault
        )
    }

    private fun com.dairum.data.local.entity.CategoryWithCount.toDomainWithCount(): Category {
        return Category(
            id = category.id,
            name = category.name,
            color = category.color,
            icon = category.icon,
            sortOrder = category.sortOrder,
            isDefault = category.isDefault,
            entryCount = entryCount
        )
    }

    private fun Category.toEntity(): CategoryEntity {
        return CategoryEntity(
            id = id,
            name = name,
            color = color,
            icon = icon,
            sortOrder = sortOrder,
            isDefault = isDefault
        )
    }
}
