package com.mvvm.girishdemo.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.mvvm.girishdemo.R

/**
 * Created by Girish Sigicherla on 2/25/2020.
 */
abstract class BaseActivity(private val backButton: Boolean) : AppCompatActivity() {
    @LayoutRes
    abstract fun getLayoutResId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        if (backButton) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }


    override fun onBackPressed() {
        (supportFragmentManager.findFragmentById(
            R.id.fragmentContainer
        ) as BaseFragment).onBackPressed()
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}