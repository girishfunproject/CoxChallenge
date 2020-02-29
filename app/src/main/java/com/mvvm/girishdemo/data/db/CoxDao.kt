package com.mvvm.girishdemo.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mvvm.girishdemo.model.DataSetId
import com.mvvm.girishdemo.model.Dealer
import com.mvvm.girishdemo.model.Vehicle
import io.reactivex.Single

/**
 * Created by Girish Sigicherla on 2/26/2020.
 */
@Dao
interface CoxDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVehicleInfo(vehicle: Vehicle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDealerInfo(dealer: Dealer)

    @Query("SELECT * FROM vehicles ORDER BY year")
    fun queryAllVehicles(): Single<List<Vehicle>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDataSetId(dataSetId: DataSetId)

    @Query("Select id from datasetId")
    fun fetchDataSetId(): Single<String>

}