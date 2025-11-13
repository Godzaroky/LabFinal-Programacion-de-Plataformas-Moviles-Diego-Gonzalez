package com.example.uvg.gonzalez.diego.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.uvg.gonzalez.diego.domain.AssetDetail
import com.example.uvg.gonzalez.diego.domain.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetDetailScreen(
    state: UiState<AssetDetail>,
    modeLabel: String?,
    onBack: () -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Asset") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (state) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(state.message)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onRetry) {
                            Text("Reintentar")
                        }
                    }
                }

                is UiState.Success -> {
                    val asset = state.data
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = asset.name,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = asset.symbol,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Precio: $${asset.priceUsd}")
                        Text("Cambio 24h: ${asset.changePercent24Hr}%")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Supply: ${asset.supply ?: "-"}")
                        Text("Max Supply: ${asset.maxSupply ?: "-"}")
                        Text("Market Cap: ${asset.marketCapUsd ?: "-"}")

                        if (modeLabel != null) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = modeLabel,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
