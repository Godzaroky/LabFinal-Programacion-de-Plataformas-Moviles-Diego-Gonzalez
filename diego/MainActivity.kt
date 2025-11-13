package com.example.uvg.gonzalez.diego

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.example.uvg.gonzalez.diego.data.network.AssetsRepository
import com.example.uvg.gonzalez.diego.data.network.local.AppDatabase
import com.example.uvg.gonzalez.diego.data.network.AssetsRemoteDataSourceImpl
import com.example.uvg.gonzalez.diego.data.network.provideCoinCapClient
import com.example.uvg.gonzalez.diego.data.network.settings.SettingsRepository
import com.example.uvg.gonzalez.diego.ui.CryptoApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "assets-db"
        ).build()

        val settingsRepository = SettingsRepository(applicationContext)
        val client = provideCoinCapClient()
        val remote = AssetsRemoteDataSourceImpl(client)
        val repository = AssetsRepository(
            remote = remote,
            assetDao = db.assetDao(),
            settingsRepository = settingsRepository
        )

        setContent {
            // Si quieres, aquí puedes envolver con tu Theme generado
            CryptoApp(repository = repository)
        }
    }
}