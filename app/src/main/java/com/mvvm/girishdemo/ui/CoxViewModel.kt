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
class CoxViewModel @Inject constructor(
    private val coxRepository: CoxRepository
) : ViewModel() {

    var errorString: MutableLiveData<String> = MutableLiveData()

    var vehicleListResult: MutableLiveData<List<Vehicle>> = MutableLiveData()
    private lateinit var vehicleListDisposableObserver: DisposableObserver<Vehicle>

    var dealerListResult: MutableLiveData<List<Dealer>> = MutableLiveData()
    private lateinit var dealerListDisposableObserver: DisposableObserver<Dealer>


    fun getVehicleListResult(): LiveData<List<Vehicle>> = vehicleListResult

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
            ?.flatMap { list ->
                Observable.fromIterable(list.vehicleIds).flatMap {
                    coxRepository.getVehicleInfoFromApi(it)
                }
            }
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(vehicleListDisposableObserver)
    }

    fun getDealerListResult(): LiveData<List<Dealer>> = dealerListResult

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

}