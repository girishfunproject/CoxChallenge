package com.mvvm.girishdemo.data

import com.mvvm.girishdemo.data.db.CoxDao
import com.mvvm.girishdemo.data.server.ApiInterface
import com.mvvm.girishdemo.model.DataSetId
import com.mvvm.girishdemo.model.Dealer
import com.mvvm.girishdemo.model.Vehicle
import com.mvvm.girishdemo.model.VehicleIdList
import com.mvvm.girishdemo.utils.Utils
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Girish Sigicherla on 2/26/2020.
 */
class CoxRepository @Inject constructor(
    private val apiInterface: ApiInterface,
    private val coxDao: CoxDao,
    private val utils: Utils
) {

    private fun getDataSetIdFromDB(): String =
        coxDao.fetchDataSetId().toObservable().blockingFirst()

    fun getDealerIdsFromDB(): Observable<List<Int>> =
        coxDao.fetchDealerIdsFromVehicle().toObservable()

    fun getAllDealersFromDB(): Observable<List<Dealer>> = coxDao.queryAllDealers().toObservable()

    fun getVehicleListFromApi(): Observable<VehicleIdList> {
        return getDataSetIdFromApi().flatMap {
            getVehicleList(getDataSetIdFromDB())
        }
    }

    private fun getDataSetIdFromApi(): Observable<DataSetId> {
        return apiInterface.getDataSetId()
            .doOnNext {
                coxDao.insertDataSetId(it)
            }
    }

    private fun getVehicleList(id: String): Observable<VehicleIdList> {
        return if (utils.isConnectedToInternet()) {
            apiInterface.getVehicleList(id)
        } else {
            Observable.error(Throwable("No Internet"))
        }
    }

    fun getVehicleInfoFromApi(vehicleId: String): Observable<Vehicle> {
        return if (utils.isConnectedToInternet()) {
            apiInterface.getVehicleInfo(getDataSetIdFromDB(), vehicleId)
                .doOnNext {
                    //save to database
                    coxDao.insertVehicleInfo(it)
                }
        } else {
            Observable.error(Throwable("No Internet"))
        }
    }

    fun getDealerInfoFromApi(dealerId: Int): Observable<Dealer> {
        return if (utils.isConnectedToInternet()) {
            apiInterface.getDealerInfo(getDataSetIdFromDB(), dealerId)
                .doOnNext {
                    coxDao.insertDealerInfo(it)
                }
        } else {
            Observable.error(Throwable("No Internet"))
        }
    }

    fun getAllVehicleInfoFromDB(): Observable<List<Vehicle>> =
        coxDao.queryAllVehicles().toObservable()

    fun getAllVehicleForDealerFromDB(dealerId: Int): Observable<List<Vehicle>> =
        coxDao.queryAllVehiclesForDealer(dealerId).toObservable()
}