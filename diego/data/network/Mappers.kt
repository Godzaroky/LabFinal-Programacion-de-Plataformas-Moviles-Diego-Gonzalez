package com.example.uvg.gonzalez.diego.data.network

import com.example.uvg.gonzalez.diego.data.network.local.AssetEntity
import com.example.uvg.gonzalez.diego.data.network.dto.AssetDto
import com.example.uvg.gonzalez.diego.domain.AssetDetail
import com.example.uvg.gonzalez.diego.domain.AssetSummary

fun AssetDto.toSummary(): AssetSummary =
    AssetSummary(
        id = id,
        name = name,
        symbol = symbol,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr
    )

fun AssetDto.toDetail(): AssetDetail =
    AssetDetail(
        id = id,
        name = name,
        symbol = symbol,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr,
        supply = supply,
        maxSupply = maxSupply,
        marketCapUsd = marketCapUsd
    )

fun AssetEntity.toSummary(): AssetSummary =
    AssetSummary(
        id = id,
        name = name,
        symbol = symbol,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr
    )

fun AssetEntity.toDetail(): AssetDetail =
    AssetDetail(
        id = id,
        name = name,
        symbol = symbol,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr,
        supply = supply,
        maxSupply = maxSupply,
        marketCapUsd = marketCapUsd
    )

fun AssetDetail.toEntity(): AssetEntity =
    AssetEntity(
        id = id,
        name = name,
        symbol = symbol,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr,
        supply = supply,
        maxSupply = maxSupply,
        marketCapUsd = marketCapUsd
    )
