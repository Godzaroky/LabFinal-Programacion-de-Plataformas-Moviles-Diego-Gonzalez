package com.example.uvg.gonzalez.diego.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.uvg.gonzalez.diego.domain.AssetSummary
import com.example.uvg.gonzalez.diego.domain.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetsListScreen(
    state: UiState<List<AssetSummary>>,
    modeLabel: String?,
    onRetry: () -> Unit,
    onSaveOffline: () -> Unit,
    onAssetClick: (AssetSummary) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Criptomonedas") }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onSaveOffline
            ) {
                Text("Ver offline")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (modeLabel != null) {
                Text(
                    text = modeLabel,
                    modifier = Modifier
                        .padding(8.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }

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
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = state.message)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = onRetry) {
                                Text("Reintentar")
                            }
                        }
                    }
                }

                is UiState.Success -> {
                    LazyColumn {
                        items(state.data) { asset ->
                            AssetRow(
                                asset = asset,
                                onClick = { onAssetClick(asset) }
                            )
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AssetRow(
    asset: AssetSummary,
    onClick: () -> Unit
) {
    val change = asset.changePercent24Hr.toDoubleOrNull()
    val isPositive = (change ?: 0.0) >= 0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(asset.name, style = MaterialTheme.typography.titleMedium)
            Text(asset.symbol, style = MaterialTheme.typography.bodySmall)
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(text = "$${asset.priceUsd.take(10)}")
            Text(
                text = "${asset.changePercent24Hr.take(7)}%",
                color = if (isPositive) Color(0xFF2ECC71) else Color(0xFFE74C3C)
            )
        }
    }
}