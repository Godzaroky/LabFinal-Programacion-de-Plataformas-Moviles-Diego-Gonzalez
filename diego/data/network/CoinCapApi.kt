package com.example.uvg.gonzalez.diego.data.network

import com.example.uvg.gonzalez.diego.data.network.dto.AssetByIdResponse
import com.example.uvg.gonzalez.diego.data.network.dto.AssetDto
import com.example.uvg.gonzalez.diego.data.network.dto.AssetsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.gson.gson

fun provideCoinCapClient(): HttpClient {
    return HttpClient(Android) {
        install(ContentNegotiation) {
            gson()
        }
        defaultRequest {
            // Solo el header, SIN url base
            header(
                "Authorization",
                "Bearer 6f8c2f757cc81e9950a05aeed8292abff853114ebc731977f3f5a580b1e9371a"
            )
        }
    }
}

interface AssetsRemoteDataSource {
    suspend fun getAssets(): List<AssetDto>
    suspend fun getAssetById(id: String): AssetDto
}

class AssetsRemoteDataSourceImpl(
    private val client: HttpClient
) : AssetsRemoteDataSource {

    override suspend fun getAssets(): List<AssetDto> {
        val response: AssetsResponse =
            client.get("https://rest.coincap.io/v3/assets").body()
        return response.data
    }

    override suspend fun getAssetById(id: String): AssetDto {
        val response: AssetByIdResponse =
            client.get("https://rest.coincap.io/v3/assets/$id").body()
        return response.data
    }
}
