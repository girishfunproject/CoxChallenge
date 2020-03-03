package com.mvvm.girishdemo.ui.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.girishdemo.R
import com.mvvm.girishdemo.model.Dealer
import java.util.Collections.emptyList

/**
 * Created by Girish Sigicherla on 3/1/2020.
 */

/**
 * A standard adapter used to display a list of dealers. This adapter takes the dealers provided by the view model and serves those to the @see[CoxRecyclerView]
 */
class DealerListAdapter(var onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<DealerListAdapter.DealerViewHolder>() {

    /**
     * List of dealers that will be populated in the recycler view
     */
    var dealers: List<Dealer> = emptyList()

    /**
     * Called when RecyclerView needs a new @see[DealerViewHolder] to represent an item.
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealerViewHolder {
        val dealerView =
            LayoutInflater.from(parent.context).inflate(R.layout.dealer_item_view, parent, false)
        return DealerViewHolder(dealerView)
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: DealerViewHolder, position: Int) =
        holder.bind(dealers[position])

    /**
     * Returns the total number of items in the data set hold by the adapter.
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int = dealers.size

    inner class DealerViewHolder(private var dealerView: View) :
        RecyclerView.ViewHolder(dealerView) {
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

/**
 * To provide callback mechanism to launch VehiclesFragment and to pass the dealer id when a dealer item is clicked
 */
interface OnItemClickListener {
    fun onItemClicked(dealerId: Int)
}