package com.mvvm.girishdemo.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by Girish Sigicherla on 2/25/2020.
 */
abstract class BaseActivity : AppCompatActivity() {
    @LayoutRes
    abstract fun getLayoutResId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
    }

}