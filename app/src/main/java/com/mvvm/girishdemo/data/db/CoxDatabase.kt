package com.mvvm.girishdemo.data.db

import com.mvvm.girishdemo.model.Vehicle
import androidx.room.Database
import androidx.room.RoomDatabase
import com.mvvm.girishdemo.model.DataSetId
import com.mvvm.girishdemo.model.Dealer

/**
 * Created by Girish Sigicherla on 2/26/2020.
 */
@Database(entities = [Vehicle::class, DataSetId::class, Dealer::class], version = 8)
abstract class CoxDatabase : RoomDatabase() {
    abstract fun coxDao(): CoxDao
}