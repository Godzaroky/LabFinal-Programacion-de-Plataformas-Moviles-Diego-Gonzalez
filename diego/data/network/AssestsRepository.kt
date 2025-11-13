package com.example.uvg.gonzalez.diego.data.network

import com.example.uvg.gonzalez.diego.data.network.local.AssetDao
import com.example.uvg.gonzalez.diego.data.network.settings.SettingsRepository
import com.example.uvg.gonzalez.diego.domain.AssetDetail
import com.example.uvg.gonzalez.diego.domain.AssetSummary
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AssetsRepository(
    private val remote: AssetsRemoteDataSource,
    private val assetDao: AssetDao,
    private val settingsRepository: SettingsRepository
) {

    suspend fun fetchAssetsOnline(): List<AssetSummary> {
        val dtos = remote.getAssets()
        return dtos.map { it.toSummary() }
    }

    suspend fun fetchAssetDetailOnline(id: String): AssetDetail {
        val dto = remote.getAssetById(id)
        return dto.toDetail()
    }

    suspend fun saveAssetsOffline(details: List<AssetDetail>) {
        val entities = details.map { it.toEntity() }
        assetDao.insertAll(entities)
        val now = Instant.now().atZone(ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val formatted = now.format(formatter)
        settingsRepository.saveLastSyncDateTime(formatted)
    }

    suspend fun getAssetsOffline(): List<AssetSummary> {
        return assetDao.getAll().map { it.toSummary() }
    }

    suspend fun getAssetDetailOffline(id: String): AssetDetail? {
        return assetDao.getById(id)?.toDetail()
    }

    suspend fun getLastSyncLabel(): String? {
        return settingsRepository.lastSyncDateTime.first()
    }
}
