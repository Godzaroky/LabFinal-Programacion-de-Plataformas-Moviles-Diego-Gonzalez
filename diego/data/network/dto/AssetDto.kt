package com.example.uvg.gonzalez.diego.data.network.dto

data class AssetsResponse(
    val data: List<AssetDto>
)

data class AssetByIdResponse(
    val data: AssetDto
)

data class AssetDto(
    val id: String,
    val rank: String? = null,
    val symbol: String,
    val name: String,
    val supply: String? = null,
    val maxSupply: String? = null,
    val marketCapUsd: String? = null,
    val volumeUsd24Hr: String? = null,
    val priceUsd: String,
    val changePercent24Hr: String,
    val vwap24Hr: String? = null
)
