package com.mvvm.girishdemo.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mvvm.girishdemo.R
import com.mvvm.girishdemo.base.BaseActivity
import com.mvvm.girishdemo.model.Vehicle
import com.mvvm.girishdemo.utils.Utils
import com.mvvm.girishdemo.utils.gone
import com.mvvm.girishdemo.utils.visible
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
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
        if (savedInstanceState != null) {
            return
        }
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, HomeFragment(utils)).commit()
    }

    fun getViewModel(): CoxViewModel {
        return coxViewModel
    }

    override fun onDestroy() {
        coxViewModel.disposeElements()
        super.onDestroy()
    }

}

