package com.mvvm.girishdemo.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mvvm.girishdemo.data.CoxRepository
import com.mvvm.girishdemo.model.Vehicle
import com.mvvm.girishdemo.model.VehicleIdList
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by Girish Sigicherla on 2/26/2020.
 */
class CoxViewModel @Inject constructor(
    private val coxRepository: CoxRepository
) : ViewModel() {

    var errorString: MutableLiveData<String> = MutableLiveData()

    var vehicleListResult: MutableLiveData<List<Vehicle>> = MutableLiveData()
    lateinit var vehicleListDisposableObserver: DisposableObserver<Vehicle>

    var vehicleInfoListDBResult: MutableLiveData<List<Vehicle>> = MutableLiveData()
    var vehicleInfoListApiResult: MutableLiveData<List<Vehicle>> = MutableLiveData()
    lateinit var vehicleInfoListDBDisposableObserver: DisposableObserver<List<Vehicle>>
    lateinit var vehicleInfoListApiDisposableObserver: DisposableObserver<List<Vehicle>>


    fun getVehicleListResult(): LiveData<List<Vehicle>> = vehicleListResult

    //fetching from API and not DB
    @RequiresApi(Build.VERSION_CODES.N)
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
            ?.doOnComplete {

            }
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(vehicleListDisposableObserver)
    }


    fun getVehicleInfoListFromApi(vehicleIdList: List<String>?) {
        coxRepository.getVehicleInfoListFromApi(vehicleIdList)
    }

    fun getVehicleInfoListResult(): LiveData<List<Vehicle>> = vehicleInfoListDBResult

    fun getAllVehicleInfoListFromDb() {
        vehicleInfoListDBDisposableObserver = object : DisposableObserver<List<Vehicle>>() {
            override fun onComplete() {
            }

            override fun onNext(list: List<Vehicle>) {
                vehicleInfoListDBResult.postValue(list)
            }

            override fun onError(e: Throwable) {
                errorString.postValue(e.message)
            }
        }
        coxRepository.getAllVehicleInfofromDB()
            .subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(vehicleInfoListDBDisposableObserver)
    }


}