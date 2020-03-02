package com.mvvm.girishdemo.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.mvvm.girishdemo.R
import com.mvvm.girishdemo.base.BaseActivity
import com.mvvm.girishdemo.utils.Utils
import dagger.android.AndroidInjection
import javax.inject.Inject


class MainActivity : BaseActivity() {

    override fun getLayoutResId(): Int = R.layout.activity_main

    @Inject
    lateinit var coxViewModelFactory: CoxViewModelFactory
    lateinit var coxViewModel: CoxViewModel
    lateinit var utils: Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        utils = Utils(this)
        coxViewModel =
            ViewModelProviders.of(this, coxViewModelFactory).get(CoxViewModel::class.java)
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, HomeFragment(utils))
            .commit()
    }

    fun getViewModel(): CoxViewModel {
        return coxViewModel
    }

    override fun onDestroy() {
        coxViewModel.disposeElements()
        super.onDestroy()
    }

}

