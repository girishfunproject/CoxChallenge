package com.mvvm.girishdemo.ui.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.girishdemo.R
import com.mvvm.girishdemo.model.Dealer
import java.util.Collections.emptyList

/**
 * Created by Girish Sigicherla on 3/1/2020.
 */

class DealerListAdapter(var onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<DealerListAdapter.DealerViewHolder>() {

    var dealers: List<Dealer> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealerViewHolder {
        val dealerView =
            LayoutInflater.from(parent.context).inflate(R.layout.dealer_item_view, parent, false)
        return DealerViewHolder(dealerView)
    }

    override fun onBindViewHolder(holder: DealerViewHolder, position: Int) =
        holder.bind(dealers[position])

    override fun getItemCount(): Int = dealers.size

    inner class DealerViewHolder(var dealerView: View) : RecyclerView.ViewHolder(dealerView) {
        private val dealerName: TextView = dealerView.findViewById(R.id.dealer_name)
        private val dealerId: TextView = dealerView.findViewById(R.id.dealer_id)

        fun bind(dealer: Dealer) {
            dealerName.text = dealer.name
            dealerId.text = dealer.dealerId.toString()
            dealerView.setOnClickListener {
                onItemClickListener.onItemClicked(dealer.dealerId)
            }
        }
    }
}

interface OnItemClickListener {
    fun onItemClicked(dealerId: Int)
}