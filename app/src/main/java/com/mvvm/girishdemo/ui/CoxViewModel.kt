package com.mvvm.girishdemo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mvvm.girishdemo.data.CoxRepository
import com.mvvm.girishdemo.model.Dealer
import com.mvvm.girishdemo.model.Vehicle
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Girish Sigicherla on 2/26/2020.
 */

/**
 * This view model class contains the data required to be populated on the views.
 * It exposes public methods and live data objects to be observed by the views.
 * It uses observables to notify the view about data changes.
 * It is completely agnostic of the view.
 */
class CoxViewModel @Inject constructor(
    private val coxRepository: CoxRepository
) : ViewModel() {

    var errorString: MutableLiveData<String> = MutableLiveData()

    var vehicleListResult: MutableLiveData<List<Vehicle>> = MutableLiveData()
    private lateinit var vehicleListDisposableObserver: DisposableObserver<Vehicle>

    var vehicleListDBResult: MutableLiveData<List<Vehicle>> = MutableLiveData()
    private lateinit var vehicleListDBDisposableObserver: DisposableObserver<List<Vehicle>>

    var vehicleListForDealerDBResult: MutableLiveData<List<Vehicle>> = MutableLiveData()
    private lateinit var vehicleListForDealerDBDisposableObserver: DisposableObserver<List<Vehicle>>

    var dealerListResult: MutableLiveData<List<Dealer>> = MutableLiveData()
    private lateinit var dealerListDisposableObserver: DisposableObserver<Dealer>

    var dealerListDBResult: MutableLiveData<List<Dealer>> = MutableLiveData()
    private lateinit var dealerListDBDisposableObserver: DisposableObserver<List<Dealer>>


    fun getVehicleListResult(): LiveData<List<Vehicle>> = vehicleListResult
    fun getVehicleListDBResult(): LiveData<List<Vehicle>> = vehicleListDBResult
    fun getVehicleListForDealerDBResult(): LiveData<List<Vehicle>> = vehicleListForDealerDBResult
    fun getDealerListResult(): LiveData<List<Dealer>> = dealerListResult
    fun getDealerListDBResult(): LiveData<List<Dealer>> = dealerListDBResult
    fun getVehicleListError(): LiveData<String> = errorString

    /**
     * To retrieve list of Vehicles from the server.
     * This method first makes an API call to get list of vehicle ids which in turn are used to fire multiple observable requests to get vehicle info for each vehicle id.
     * As each observable emits a vehicle onNext method, a vehicle list is created which can be observed by views by observing the @see[vehicleListResult] live data.
     */
    fun getVehicleList() {
        val list = ArrayList<Vehicle>()
        vehicleListDisposableObserver = object : DisposableObserver<Vehicle>() {
            override fun onComplete() {
                vehicleListResult.postValue(list)
            }

            override fun onNext(v: Vehicle) {
                list.add(v)
            }

            override fun onError(e: Throwable) {
                errorString.postValue(e.message)
            }
        }
        coxRepository.getVehicleListFromApi()
            .flatMap { list ->
                Observable.fromIterable(list.vehicleIds).flatMap {
                    coxRepository.getVehicleInfoFromApi(it)
                }
            }
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(vehicleListDisposableObserver)
    }

    /**
     * To fetch list of vehicles from the vehicles table.
     * Any view can observe the live data @see[vehicleListDBResult] to know if the vehicle list has been inserted into the database.
     */
    fun getVehicleListFromDB() {
        vehicleListDBDisposableObserver = object : DisposableObserver<List<Vehicle>>() {
            override fun onComplete() {
            }

            override fun onNext(list: List<Vehicle>) {
                vehicleListDBResult.postValue(list)
            }

            override fun onError(e: Throwable) {
            }

        }
        coxRepository.getAllVehicleInfoFromDB()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(vehicleListDBDisposableObserver)
    }

    /**
     * To retrieve list of vehicles for a given dealer from @see[Vehicle] vehicles table.
     * Any view can observe the live data @see[vehicleListForDealerDBResult] after the query is done to get a list of vehicles
     * for a given dealer.
     */
    fun getVehicleListForDealerFromDB(dealerId: Int) {
        vehicleListForDealerDBDisposableObserver = object : DisposableObserver<List<Vehicle>>() {
            override fun onComplete() {
            }

            override fun onNext(list: List<Vehicle>) {
                vehicleListForDealerDBResult.postValue(list)
            }

            override fun onError(e: Throwable) {
            }

        }
        coxRepository.getAllVehicleForDealerFromDB(dealerId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(vehicleListForDealerDBDisposableObserver)
    }

    /**
     * To retrieve the list of dealers from the server
     * Will make an api call for each dealer and when the observable emits each dealer, it will be added to a list
     * Firstly the list of dealers is fetched from the @see[Vehicle] vehicles table and then an api call is made for each dealer id to retrieve dealer inforamtion.
     * For each dealer emitted in onNext method, we construct a dealerList by the time onComplete() is called.
     * Any view can observe live data @see[dealerListResult] to know when dealerList from API is ready
     */
    fun getDealerList() {
        val dealerList = ArrayList<Dealer>()
        dealerListDisposableObserver = object : DisposableObserver<Dealer>() {
            override fun onComplete() {
                dealerListResult.postValue(dealerList)
            }

            override fun onNext(dealer: Dealer) {
                dealerList.add(dealer)
            }

            override fun onError(e: Throwable) {
            }
        }

        coxRepository.getDealerIdsFromDB()
            .flatMap { list ->
                Observable.fromIterable(list).flatMap {
                    coxRepository.getDealerInfoFromApi(it)
                }
            }
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(dealerListDisposableObserver)
    }

    /**
     * To fetch the list of dealers from the @see[Dealer] dealers table
     * Any view can observe live data @see[dealerListDBResult] after data is fetched from Database and populate it
     */
    fun getDealerListFromDB() {
        dealerListDBDisposableObserver = object : DisposableObserver<List<Dealer>>() {
            override fun onComplete() {
            }

            override fun onNext(list: List<Dealer>) {
                dealerListDBResult.postValue(list)
            }

            override fun onError(e: Throwable) {
            }

        }
        coxRepository.getAllDealersFromDB()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(dealerListDBDisposableObserver)

    }

    fun disposeElements() {
        if (::vehicleListDisposableObserver.isInitialized && !vehicleListDisposableObserver.isDisposed) vehicleListDisposableObserver.dispose()
        if (::vehicleListDBDisposableObserver.isInitialized && !vehicleListDBDisposableObserver.isDisposed) vehicleListDBDisposableObserver.dispose()
        if (::vehicleListForDealerDBDisposableObserver.isInitialized && !vehicleListForDealerDBDisposableObserver.isDisposed) vehicleListForDealerDBDisposableObserver.dispose()
        if (::dealerListDisposableObserver.isInitialized && !dealerListDisposableObserver.isDisposed) dealerListDisposableObserver.dispose()
        if (::dealerListDBDisposableObserver.isInitialized && !dealerListDBDisposableObserver.isDisposed) dealerListDBDisposableObserver.dispose()
    }
}