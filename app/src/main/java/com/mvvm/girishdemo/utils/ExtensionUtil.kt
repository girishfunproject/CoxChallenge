package com.mvvm.girishdemo.utils

import android.view.View

/**
 * Created by Girish Sigicherla on 2/25/2020.
 */
fun String.contains(stringList: List<String>): Boolean {
    for (string in stringList) {
        if ((this.contains(string, true))) {
            return true
        }
    }
    return false
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}
