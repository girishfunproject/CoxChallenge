package com.mvvm.girishdemo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mvvm.girishdemo.data.CoxRepository
import com.mvvm.girishdemo.model.Dealer
import com.mvvm.girishdemo.model.Vehicle
import com.mvvm.girishdemo.utils.Utils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Girish Sigicherla on 2/26/2020.
 */
class CoxViewModel @Inject constructor(
    private val coxRepository: CoxRepository,
    private val utils: Utils
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
    fun getVehicleListError(): LiveData<String> = errorString

    //fetching from API and not DB
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


    fun getDealerListResult(): LiveData<List<Dealer>> = dealerListResult

    fun getDealerListDBResult(): LiveData<List<Dealer>> = dealerListDBResult

    fun getDealerList() {
        var dealerList = ArrayList<Dealer>()
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
        if (::dealerListDisposableObserver.isInitialized && !dealerListDisposableObserver.isDisposed) dealerListDisposableObserver.dispose()
        if (::dealerListDBDisposableObserver.isInitialized && !dealerListDBDisposableObserver.isDisposed) dealerListDBDisposableObserver.dispose()
    }
}