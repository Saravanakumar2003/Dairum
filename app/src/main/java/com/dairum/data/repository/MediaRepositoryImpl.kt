package com.dairum.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import com.dairum.data.local.dao.MediaDao
import com.dairum.data.local.entity.MediaEntity
import com.dairum.domain.model.Media
import com.dairum.domain.model.MediaType
import com.dairum.domain.repository.MediaRepository
import com.dairum.domain.repository.MediaWithEntryInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val mediaDao: MediaDao,
    @ApplicationContext private val context: Context
) : MediaRepository {

    override suspend fun addMedia(media: Media): Long {
        return mediaDao.insert(media.toEntity())
    }

    override suspend fun addMediaList(mediaList: List<Media>): List<Long> {
        return mediaDao.insertAll(mediaList.map { it.toEntity() })
    }

    override suspend fun updateMedia(media: Media) {
        mediaDao.update(media.toEntity())
    }

    override suspend fun deleteMedia(id: Long) {
        val media = mediaDao.getById(id)
        if (media != null) {
            // Delete physical file
            deleteMediaFile(media.uri)
            media.thumbnailUri?.let { deleteMediaFile(it) }
        }
        mediaDao.deleteById(id)
    }

    override suspend fun deleteAllForEntry(entryId: Long) {
        val mediaList = mediaDao.getMediaForEntry(entryId)
        mediaList.forEach { media ->
            deleteMediaFile(media.uri)
            media.thumbnailUri?.let { deleteMediaFile(it) }
        }
        mediaDao.deleteAllForEntry(entryId)
    }

    override suspend fun getMediaById(id: Long): Media? {
        return mediaDao.getById(id)?.toDomain()
    }

    override suspend fun getMediaForEntry(entryId: Long): List<Media> {
        return mediaDao.getMediaForEntry(entryId).map { it.toDomain() }
    }

    override fun getMediaForEntryFlow(entryId: Long): Flow<List<Media>> {
        return mediaDao.getMediaForEntryFlow(entryId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getPhotosByEntryFlow(): Flow<List<MediaWithEntryInfo>> {
        return mediaDao.getMediaWithEntryByTypeFlow(
            com.dairum.data.local.entity.MediaType.PHOTO
        ).map { list ->
            list.map { it.toDomainWithEntry() }
        }
    }

    override fun getVideosByEntryFlow(): Flow<List<MediaWithEntryInfo>> {
        return mediaDao.getMediaWithEntryByTypeFlow(
            com.dairum.data.local.entity.MediaType.VIDEO
        ).map { list ->
            list.map { it.toDomainWithEntry() }
        }
    }

    override fun getVoiceNotesByEntryFlow(): Flow<List<MediaWithEntryInfo>> {
        return mediaDao.getMediaWithEntryByTypeFlow(
            com.dairum.data.local.entity.MediaType.VOICE
        ).map { list ->
            list.map { it.toDomainWithEntry() }
        }
    }

    override fun getAttachmentsByEntryFlow(): Flow<List<MediaWithEntryInfo>> {
        return mediaDao.getMediaWithEntryByTypeFlow(
            com.dairum.data.local.entity.MediaType.ATTACHMENT
        ).map { list ->
            list.map { it.toDomainWithEntry() }
        }
    }

    override suspend fun getCountByType(type: MediaType): Int {
        return mediaDao.getCountByType(type.toEntity())
    }

    override suspend fun getTotalSize(): Long {
        return mediaDao.getTotalSize() ?: 0L
    }

    override suspend fun getSizeByType(type: MediaType): Long {
        return mediaDao.getSizeByType(type.toEntity()) ?: 0L
    }

    override suspend fun saveMediaFile(uri: String, entryId: Long, type: MediaType): Media = withContext(Dispatchers.IO) {
        val contentUri = Uri.parse(uri)
        
        // Get file info from content resolver
        val fileName = getFileName(contentUri) ?: "file_${UUID.randomUUID()}"
        val mimeType = context.contentResolver.getType(contentUri) ?: getMimeTypeFromExtension(fileName)
        val fileSize = getFileSize(contentUri)
        
        // Create directory for media type
        val mediaDir = File(context.filesDir, type.name.lowercase())
        if (!mediaDir.exists()) {
            mediaDir.mkdirs()
        }
        
        // Generate unique file name
        val extension = MimeTypeMap.getFileExtensionFromUrl(uri) 
            ?: fileName.substringAfterLast('.', "")
        val uniqueFileName = "${UUID.randomUUID()}.$extension"
        val destFile = File(mediaDir, uniqueFileName)
        
        // Copy file to app storage
        context.contentResolver.openInputStream(contentUri)?.use { inputStream ->
            FileOutputStream(destFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        
        Media(
            entryId = entryId,
            type = type,
            uri = destFile.absolutePath,
            fileName = fileName,
            mimeType = mimeType,
            sizeBytes = fileSize
        )
    }
    
    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index >= 0) {
                        result = cursor.getString(index)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path?.substringAfterLast('/')
        }
        return result
    }
    
    private fun getFileSize(uri: Uri): Long {
        var size = 0L
        if (uri.scheme == "content") {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(OpenableColumns.SIZE)
                    if (index >= 0) {
                        size = cursor.getLong(index)
                    }
                }
            }
        }
        return size
    }
    
    private fun getMimeTypeFromExtension(fileName: String): String {
        val extension = fileName.substringAfterLast('.', "").lowercase()
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "application/octet-stream"
    }

    override suspend fun createThumbnail(mediaId: Long): String? {
        // TODO: Generate thumbnail for photo/video
        return null
    }

    override suspend fun deleteMediaFile(uri: String) {
        try {
            val file = java.io.File(uri)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            // Log error
        }
    }

    private fun MediaEntity.toDomain(): Media {
        return Media(
            id = id,
            entryId = entryId,
            type = type.toDomain(),
            uri = uri,
            thumbnailUri = thumbnailUri,
            fileName = fileName,
            mimeType = mimeType,
            sizeBytes = sizeBytes,
            duration = duration,
            width = width,
            height = height,
            caption = caption,
            sortOrder = sortOrder
        )
    }

    private fun com.dairum.data.local.entity.MediaWithEntry.toDomainWithEntry(): MediaWithEntryInfo {
        return MediaWithEntryInfo(
            media = media.toDomain(),
            entryTitle = entryTitle,
            entryCreatedAt = entryCreatedAt
        )
    }

    private fun Media.toEntity(): MediaEntity {
        return MediaEntity(
            id = id,
            entryId = entryId,
            type = type.toEntity(),
            uri = uri,
            thumbnailUri = thumbnailUri,
            fileName = fileName,
            mimeType = mimeType,
            sizeBytes = sizeBytes,
            duration = duration,
            width = width,
            height = height,
            caption = caption,
            sortOrder = sortOrder
        )
    }

    private fun com.dairum.data.local.entity.MediaType.toDomain(): MediaType {
        return when (this) {
            com.dairum.data.local.entity.MediaType.PHOTO -> MediaType.PHOTO
            com.dairum.data.local.entity.MediaType.VIDEO -> MediaType.VIDEO
            com.dairum.data.local.entity.MediaType.VOICE -> MediaType.VOICE
            com.dairum.data.local.entity.MediaType.ATTACHMENT -> MediaType.ATTACHMENT
        }
    }

    private fun MediaType.toEntity(): com.dairum.data.local.entity.MediaType {
        return when (this) {
            MediaType.PHOTO -> com.dairum.data.local.entity.MediaType.PHOTO
            MediaType.VIDEO -> com.dairum.data.local.entity.MediaType.VIDEO
            MediaType.VOICE -> com.dairum.data.local.entity.MediaType.VOICE
            MediaType.ATTACHMENT -> com.dairum.data.local.entity.MediaType.ATTACHMENT
        }
    }
}
