package com.mvvm.girishdemo.di

import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mvvm.girishdemo.data.db.CoxDao
import com.mvvm.girishdemo.ui.CoxViewModelFactory
import com.mvvm.girishdemo.utils.Constants
import com.mvvm.girishdemo.utils.Utils
import android.app.Application
import com.mvvm.girishdemo.data.db.CoxDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Girish Sigicherla on 2/26/2020.
 */


@Module
class AppModule(val app: Application) {

    companion object {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Change the table name to the correct one
                database.execSQL("ALTER TABLE Coxdb1 RENAME TO Coxdb2")
            }
        }
    }

    @Provides
    @Singleton
    fun provideApplication(): Application = app

    @Provides
    @Singleton
    fun provideCoxDatabase(app: Application): CoxDatabase = Room.databaseBuilder(
        app,
        CoxDatabase::class.java, Constants.DATABASE_NAME)
        /*.addMigrations(MIGRATION_1_2)*/
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideCoxDao(
        database: CoxDatabase
    ): CoxDao = database.coxDao()

    @Provides
    @Singleton
    fun provideCoxViewModelFactory(
        factory: CoxViewModelFactory
    ): ViewModelProvider.Factory = factory

    @Provides
    @Singleton
    fun provideUtils(): Utils = Utils(app)
}