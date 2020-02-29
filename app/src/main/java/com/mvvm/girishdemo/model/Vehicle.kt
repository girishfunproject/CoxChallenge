package com.mvvm.girishdemo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

/**
 * Created by Girish Sigicherla on 2/26/2020.
 */

@Entity(
    tableName = "vehicles"
)
data class Vehicle(

    @Json(name = "vehicleId")
    @PrimaryKey
    @ColumnInfo(name = "vehicleId")
    val vehicleId: Int,

    @Json(name = "year")
    @ColumnInfo(name = "year")
    val year: Int,

    @Json(name = "make")
    @ColumnInfo(name = "make")
    val make: String,

    @Json(name = "model")
    @ColumnInfo(name = "model")
    val model: String,

    @Json(name = "dealerId")
    @ColumnInfo(name = "dealerId")
    val dealerId: Int
) : Serializable