package com.example.uvg.gonzalez.diego.ui.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.uvg.gonzalez.diego.data.network.AssetsRepository
import com.example.uvg.gonzalez.diego.domain.AssetDetail
import com.example.uvg.gonzalez.diego.domain.UiState
import kotlinx.coroutines.launch

class AssetDetailViewModel(
    private val repository: AssetsRepository,
    private val assetId: String
) : ViewModel() {

    var uiState by mutableStateOf<UiState<AssetDetail>>(UiState.Loading)
        private set

    var modeLabel by mutableStateOf<String?>(null)
        private set

    init {
        loadDetail()
    }

    fun loadDetail() {
        viewModelScope.launch {
            uiState = UiState.Loading
            try {
                val online = repository.fetchAssetDetailOnline(assetId)
                uiState = UiState.Success(online)
                modeLabel = "Viendo data más reciente"
            } catch (e: Exception) {
                val offline = repository.getAssetDetailOffline(assetId)
                if (offline != null) {
                    uiState = UiState.Success(offline)
                    val lastSync = repository.getLastSyncLabel()
                    modeLabel = lastSync?.let { "Viendo data del $it" }
                } else {
                    uiState = UiState.Error("No se pudo cargar información")
                    modeLabel = null
                }
            }
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(
            repository: AssetsRepository,
            assetId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AssetDetailViewModel(repository, assetId) as T
            }
        }
    }
}
