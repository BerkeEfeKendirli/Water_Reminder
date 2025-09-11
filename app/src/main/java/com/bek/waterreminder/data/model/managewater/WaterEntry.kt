package com.bek.waterreminder.data.model.managewater

import java.util.UUID
import kotlinx.serialization.Serializable

@Serializable
data class WaterEntry(
    val id: String = UUID.randomUUID().toString(),
    val amount: Int,
    val time: String,
)
