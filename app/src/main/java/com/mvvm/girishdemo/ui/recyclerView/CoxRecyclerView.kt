package com.mvvm.girishdemo.ui.recyclerView

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.girishdemo.R

/**
 * Created by Girish Sigicherla on 3/1/2020.
 */

/**
 * The CoxRecyclerView class can be used to display a list of view holders.
 * This view can present the items either horizontally or vertically based on the view configuration.
 * Uses a LinearLayoutManager as a Layout manager to display items horizontally or vertically.
 */
class CoxRecyclerView : RecyclerView {

    var isHorizontal: Boolean = false
        set(value) {
            field = value
            setUpView()
        }

    constructor(context: Context) : super(context) {
        setUpView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        processAttributes(context, attrs)
        setUpView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        processAttributes(context, attrs)
        setUpView()
    }

    /**
     * Handles custom processing for attributes on this view.
     *
     * We specifically handle the standard orientation attribute to determine which layout manager
     * to use.
     */
    private fun processAttributes(context: Context, attrs: AttributeSet) {
        val attrIDs = context.obtainStyledAttributes(attrs, R.styleable.RecyclerView)
        if (attrIDs.hasValue(R.styleable.RecyclerView_android_orientation)) {
            val orientation = attrIDs.getInt(R.styleable.RecyclerView_android_orientation, VERTICAL)
            isHorizontal = (orientation == VERTICAL)
        }
        attrIDs.recycle()
    }

    /**
     * Determines which layout manager to use for the  view based on its orientation.
     */
    private fun setUpView() {
        when {
            isHorizontal -> {
                layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
            }
            else -> {
                isVerticalScrollBarEnabled = true
                layoutManager = LinearLayoutManager(context, VERTICAL, false)

            }
        }
    }

    private companion object {
        private val TAG = CoxRecyclerView::class.java.simpleName
    }
}