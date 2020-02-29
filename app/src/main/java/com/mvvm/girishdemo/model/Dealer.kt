package com.mvvm.girishdemo.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json


/**
 * Created by Girish Sigicherla on 2/26/2020.
 */

@Entity(
    tableName = "dealers"
)
data class Dealer(
    @Json(name = "dealerId")
    @PrimaryKey
    @ColumnInfo(name = "dealerId")
    val dealerId: Int,

    @Json(name = "name")
    @ColumnInfo(name = "name")
    val name: String
)