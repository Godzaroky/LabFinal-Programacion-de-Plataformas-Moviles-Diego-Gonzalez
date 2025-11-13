package com.example.uvg.gonzalez.diego.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.uvg.gonzalez.diego.data.network.AssetsRepository
import com.example.uvg.gonzalez.diego.domain.AssetSummary
import com.example.uvg.gonzalez.diego.ui.detail.AssetDetailScreen
import com.example.uvg.gonzalez.diego.ui.detail.AssetDetailViewModel
import com.example.uvg.gonzalez.diego.ui.list.AssetsListScreen
import com.example.uvg.gonzalez.diego.ui.list.AssetsListViewModel

@Composable
fun CryptoApp(
    repository: AssetsRepository
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "assets"
    ) {
        composable(route = "assets") {
            val vm: AssetsListViewModel = viewModel(
                factory = AssetsListViewModel.provideFactory(repository)
            )
            AssetsListScreen(
                state = vm.uiState,
                modeLabel = vm.modeLabel,
                onRetry = { vm.loadAssets() },
                onSaveOffline = { vm.saveOffline() },
                onAssetClick = { asset: AssetSummary ->
                    navController.navigate("assetDetail/${asset.id}")
                }
            )
        }

        composable(
            route = "assetDetail/{assetId}",
            arguments = listOf(
                navArgument("assetId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val assetId = backStackEntry.arguments?.getString("assetId") ?: return@composable
            val vm: AssetDetailViewModel = viewModel(
                factory = AssetDetailViewModel.provideFactory(repository, assetId)
            )

            AssetDetailScreen(
                state = vm.uiState,
                modeLabel = vm.modeLabel,
                onBack = { navController.popBackStack() },
                onRetry = { vm.loadDetail() }
            )
        }
    }
}
