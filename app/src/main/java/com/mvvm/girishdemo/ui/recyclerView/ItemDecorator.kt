package com.mvvm.girishdemo.ui.recyclerView

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.girishdemo.R

/**
 * Created by Girish Sigicherla on 3/1/2020.
 */
/**
 * to add margins between recycler view items
 */
class ItemDecorator : RecyclerView.ItemDecoration {
    private var left = 0
    private var right = 0
    private var top = 0
    private var bottom = 0

    constructor(context: Context) : super() {
        init(context)
    }

    private fun init(c: Context) {
        left = c.resources.getDimensionPixelSize(R.dimen.item_margin)
        right = c.resources.getDimensionPixelSize(R.dimen.item_margin)
        top = c.resources.getDimensionPixelSize(R.dimen.item_margin)
        bottom = c.resources.getDimensionPixelSize(R.dimen.item_margin)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = left
        outRect.right = right
        outRect.top = top
        outRect.bottom = bottom
    }
}