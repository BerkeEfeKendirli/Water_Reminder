package com.bek.waterreminder.data.model.managewater

import kotlinx.serialization.Serializable

@Serializable
data class WaterEntry(
    val amount: Int,
    val time: String,
)
