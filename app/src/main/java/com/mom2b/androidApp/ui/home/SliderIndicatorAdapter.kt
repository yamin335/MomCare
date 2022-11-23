package com.mom2b.androidApp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mom2b.androidApp.R
import kotlinx.android.synthetic.main.list_item_slider_indicator.view.*

class SliderIndicatorAdapter internal constructor(
    private var noOfSlides: Int,
    private val callback: ((Int) -> Unit)
) : RecyclerView.Adapter<SliderIndicatorAdapter.SliderIndicatorViewHolder>() {

    private var checkedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderIndicatorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_slider_indicator, parent, false)
        return SliderIndicatorViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderIndicatorViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return noOfSlides
    }

    fun setNumberOfSlides(number: Int) {
        noOfSlides = number
        notifyDataSetChanged()
    }

    fun setIndicatorAt(position: Int) {
        if (position in 0 until noOfSlides) {
            checkedPosition = position
            notifyDataSetChanged()
        }
    }

    inner class SliderIndicatorViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            if (checkedPosition == adapterPosition) {
                itemView.indicator.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.theme_red))
            } else {
                itemView.indicator.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.theme_dark_blue))
            }
            itemView.setOnClickListener {
                itemView.indicator.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.theme_red))
                if (checkedPosition != adapterPosition) {
                    notifyItemChanged(checkedPosition)
                    checkedPosition = adapterPosition
                }
                callback.invoke(adapterPosition)
                notifyDataSetChanged()
            }
        }
    }
}
