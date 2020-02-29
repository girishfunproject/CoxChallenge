package com.mvvm.girishdemo.model

import com.squareup.moshi.Json
import java.io.Serializable

/**
 * Created by Girish Sigicherla on 2/27/2020.
 */
data class VehicleIdList(
    @Json(name = "vehicleIds")
    val vehicleIds: List<String>
) : Serializable