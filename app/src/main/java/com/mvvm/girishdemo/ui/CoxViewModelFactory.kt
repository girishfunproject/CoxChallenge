package com.mvvm.girishdemo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

/**
 * Created by Girish Sigicherla on 2/26/2020.
 */
class CoxViewModelFactory @Inject constructor(
    private val coxViewModel: CoxViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoxViewModel::class.java)) {
            return coxViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}