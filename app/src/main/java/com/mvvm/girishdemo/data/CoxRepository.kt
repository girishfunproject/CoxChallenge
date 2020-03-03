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

/**
 * This is a data provider class to fetch and update the data. The data can be retrieved from the Server or it can be fetched from the Database
 * @param apiInterface class that contains methods to make API calls using retrofit
 * @param coxDao data access object class that has queries to fetch or insert data into the ROOM database
 * @param utils an utility class with helper methods to know if we are connected to the internet
 */
class CoxRepository @Inject constructor(
    private val apiInterface: ApiInterface,
    private val coxDao: CoxDao,
    private val utils: Utils
) {

    /**************        Methods to fetch data from the Database      *****************/

    private fun getDataSetIdFromDB(): String =
        coxDao.fetchDataSetId().toObservable().blockingFirst()

    fun getDealerIdsFromDB(): Observable<List<Int>> =
        coxDao.fetchDealerIdsFromVehicle().toObservable()

    fun getAllDealersFromDB(): Observable<List<Dealer>> = coxDao.queryAllDealers().toObservable()

    fun getAllVehicleInfoFromDB(): Observable<List<Vehicle>> =
        coxDao.queryAllVehicles().toObservable()

    fun getAllVehicleForDealerFromDB(dealerId: Int): Observable<List<Vehicle>> =
        coxDao.queryAllVehiclesForDealer(dealerId).toObservable()


    /**************        Methods to retrieve data from the Server      *****************/

    /**
     * To make an API call to retrieve dataSetId for the current session.
     * Once the api call successfully returns, the dataSetId is stored to the @see[DataSetId] datasetId table for future use.
     * @return an Observable of type DataSetId
     */
    private fun getDataSetIdFromApi(): Observable<DataSetId> {
        return apiInterface.getDataSetId()
            .doOnNext {
                coxDao.insertDataSetId(it)
            }
    }

    /**
     * To make an API call to retrieve list of vehicle ids
     * @id dataset id for the session
     * @return an Observable of type VehicleIdList
     */
    fun getVehicleListFromApi(): Observable<VehicleIdList> {
        return getDataSetIdFromApi().flatMap {
            getVehicleList(getDataSetIdFromDB())
        }
    }

    private fun getVehicleList(id: String): Observable<VehicleIdList> {
        return if (utils.isConnectedToInternet()) {
            apiInterface.getVehicleList(id)
        } else {
            Observable.error(Throwable("No Internet"))
        }
    }

    /**
     * To make an API call to retrieve vehicle information for a given vehicleId.
     * Once the API call is successful, the vehicle information is stored to the @see[Vehicle] vehicles table.
     * @param vehicleId
     * @return an Observable of type Vehicle
     */
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

    /**
     * To make an API call to retrieve dealer information for a given dealerId.
     * Once the API call is successful, the dealer information is stored to the @see[Dealer] dealers table.
     * @param dealerId
     * @return an Observable of type Dealer
     */
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
}