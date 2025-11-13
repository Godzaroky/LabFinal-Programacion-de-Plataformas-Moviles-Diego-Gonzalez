package com.example.uvg.gonzalez.diego.ui.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.uvg.gonzalez.diego.data.network.AssetsRepository
import com.example.uvg.gonzalez.diego.domain.AssetDetail
import com.example.uvg.gonzalez.diego.domain.AssetSummary
import com.example.uvg.gonzalez.diego.domain.UiState
import kotlinx.coroutines.launch

class AssetsListViewModel(
    private val repository: AssetsRepository
) : ViewModel() {

    var uiState by mutableStateOf<UiState<List<AssetSummary>>>(UiState.Loading)
        private set

    var modeLabel by mutableStateOf<String?>(null)
        private set

    init {
        loadAssets()
    }

    fun loadAssets() {
        viewModelScope.launch {
            uiState = UiState.Loading
            try {
                val assetsOnline = repository.fetchAssetsOnline()
                uiState = UiState.Success(assetsOnline)
                modeLabel = "Viendo data más reciente"
            } catch (e: Exception) {
                val offline = repository.getAssetsOffline()
                if (offline.isNotEmpty()) {
                    uiState = UiState.Success(offline)
                    val lastSync = repository.getLastSyncLabel()
                    modeLabel = lastSync?.let { "Viendo data del $it" }
                } else {
                    val msg = e.localizedMessage ?: "No se pudo cargar información (error desconocido)"
                    uiState = UiState.Error("Error al cargar: $msg")
                    modeLabel = null
                }
            }
        }
    }

    fun saveOffline() {
        viewModelScope.launch {
            val current = (uiState as? UiState.Success)?.data ?: return@launch
            // Para simplificar, guardamos los datos mínimos como AssetDetail
            val details = current.map {
                AssetDetail(
                    id = it.id,
                    name = it.name,
                    symbol = it.symbol,
                    priceUsd = it.priceUsd,
                    changePercent24Hr = it.changePercent24Hr,
                    supply = null,
                    maxSupply = null,
                    marketCapUsd = null
                )
            }
            repository.saveAssetsOffline(details)
            val lastSync = repository.getLastSyncLabel()
            modeLabel = lastSync?.let { "Viendo data del $it" }
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(
            repository: AssetsRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AssetsListViewModel(repository) as T
            }
        }
    }
}
