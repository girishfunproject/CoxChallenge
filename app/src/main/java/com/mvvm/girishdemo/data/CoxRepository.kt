package com.mvvm.girishdemo.data

import android.util.Log
import com.mvvm.girishdemo.data.db.CoxDao
import com.mvvm.girishdemo.data.server.ApiInterface
import com.mvvm.girishdemo.model.DataSetId
import com.mvvm.girishdemo.model.Dealer
import com.mvvm.girishdemo.model.Vehicle
import com.mvvm.girishdemo.model.VehicleIdList
import com.mvvm.girishdemo.utils.InternetUtils
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Girish Sigicherla on 2/26/2020.
 */
class CoxRepository @Inject constructor(
    private val apiInterface: ApiInterface,
    private val coxDao: CoxDao,
    private val networkUtils: InternetUtils
) {
    //////////////////////////////////////////////////////////////////////////
    private fun getDataSetIdFromApi(): Observable<DataSetId>? {
        return apiInterface.getDataSetId()
            .doOnNext {
                coxDao.insertDataSetId(it)
            }
    }

    fun getDataSetIdFromDB(): Observable<String> {
        return coxDao.fetchDataSetId().toObservable().doOnNext {
            Log.d("data set id from DB:", it)
        }
    }

//////////////////////////////////////////////////////////////////////////

    fun getVehicleIdsObservable(): Observable<VehicleIdList>? {
        val hasConnection = networkUtils.isConnectedToInternet()
        var vehicleIdsObservable: Observable<VehicleIdList>?
        if (hasConnection) {
            vehicleIdsObservable = getVehicleListFromApi()
        } else {
            return null
        }
        return vehicleIdsObservable
    }

    fun getVehicleListFromApi(): Observable<VehicleIdList>? {
        return getDataSetIdFromApi()?.flatMap { it ->
            Log.d("Cox Repository :", it.dataSetId)
            getVehicleList(it.dataSetId)
        }?.doOnNext {
            Log.d("Cox Repository List :", it.toString())
        }
    }

    private fun getVehicleList(id: String): Observable<VehicleIdList>? {
        return if (networkUtils.isConnectedToInternet()) {
            apiInterface.getVehicleList(id)
                .doOnNext {
                }
        } else {
            null
        }
    }

//////////////////////////////////////////////////////////////////////////

    fun getVehicleInfoFromApi(vehicleId: String): Observable<Vehicle> {
        val id = getDataSetIdFromDB()
        return if (networkUtils.isConnectedToInternet()) {
            apiInterface.getVehicleInfo(id.toString(), vehicleId)
                .doOnNext {
                    //save to database
                    coxDao.insertVehicleInfo(it)
                }
        } else {
            Observable.error(Throwable("asdfasf"))
        }
    }

    fun getDealerInfoFromApi(dealerId: Int): Observable<Dealer>? {
        val id = getDataSetIdFromDB()

        return if (networkUtils.isConnectedToInternet()) {
            apiInterface.getDealerInfo(id.toString(), dealerId)
                .doOnNext {
                    coxDao.insertDealerInfo(it)
                }
        } else {
            null
        }
    }

    fun getAllVehicleInfofromDB(): Observable<List<Vehicle>> {
        return coxDao.queryAllVehicles()
            .toObservable()
            .doOnNext {
                Log.d("All vehicles from DB:", it.toString())
            }
    }

    fun getVehicleInfoListFromApi(vehicleIdList: List<String>?) {
        val id = getDataSetIdFromDB()
        if (vehicleIdList != null) {
            for (vehicleId in vehicleIdList) {
                getVehicleInfoFromApi( vehicleId)
            }
        }
    }
}