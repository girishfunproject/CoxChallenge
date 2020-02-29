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
    tableName = "datasetId"
)
data class DataSetId(
    @Json(name = "datasetId")
    @PrimaryKey
    @ColumnInfo(name = "id")
    val dataSetId: String
) : Serializable