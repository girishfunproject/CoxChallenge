package com.mvvm.girishdemo.data.server

import com.mvvm.girishdemo.model.DataSetId
import com.mvvm.girishdemo.model.Dealer
import com.mvvm.girishdemo.model.Vehicle
import com.mvvm.girishdemo.model.VehicleIdList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Girish Sigicherla on 2/26/2020.
 */
interface ApiInterface {
//http://api.coxauto-interview.com/api/

    @GET("datasetId")
    fun getDataSetId(): Observable<DataSetId>

    @GET("{datasetId}/vehicles")
    fun getVehicleList(@Path("datasetId") dataSetId: String): Observable<VehicleIdList>

    @GET("{datasetId}/vehicles/{vehicleid}")
    fun getVehicleInfo(@Path("datasetId") dataSetId: String, @Path("vehicleid") vehicleId: String): Observable<Vehicle>

    @GET("{datasetId}/dealers/{dealerid}")
    fun getDealerInfo(@Path("datasetId") dataSetId: String, @Path("dealerid") dealerId: Int): Observable<Dealer>

}