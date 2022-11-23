package com.mom2b.androidApp.ui.home

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mom2b.androidApp.R
import com.mom2b.androidApp.databinding.SliderItemBinding
import com.mom2b.androidApp.models.SiteBanner
import com.mom2b.androidApp.utils.AppConstants.MINIMUM_SLIDER_NUMBER
import com.mom2b.androidApp.utils.AppConstants.SLIDER_NUMBER_MULTIPLIER

class SliderAdapter internal constructor(
    private var itemCallback: ((SiteBanner) -> Unit)? = null,
    private var itemTouchCallback: (() -> Unit),
    private var infiniteSlidingCallback: (() -> Unit)
) : RecyclerView.Adapter<SliderAdapter.ViewHolder>() {

    private var slides: List<SiteBanner> = ArrayList()
    private var fakeItemCount = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: SliderItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_item_slider, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lastItemPosition = fakeItemCount - 1
        if (fakeItemCount > SLIDER_NUMBER_MULTIPLIER && position == lastItemPosition) {
            infiniteSlidingCallback.invoke()
        }
        val actualPosition = if (slides.size < MINIMUM_SLIDER_NUMBER) position else position % slides.size
        holder.bind(slides[actualPosition])
    }

    override fun getItemCount(): Int {
        return if (slides.size < MINIMUM_SLIDER_NUMBER) slides.size else {
            fakeItemCount = SLIDER_NUMBER_MULTIPLIER * slides.size
            fakeItemCount
        }
    }

    fun getActualItemCount(): Int {
        return slides.size
    }

    fun submitList(slides: List<SiteBanner>) {
        this.slides = slides
        notifyDataSetChanged()
    }

    inner class ViewHolder (private val binding: SliderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: SiteBanner) {
            item.title?.let { binding.sliderTitle = it }
            item.subtitle?.let { binding.sliderSubTitle = it }
            binding.url = item.image
//            binding.url = "${Api.API_ROOT_URL}/${item.image}"
            binding.imageRequestListener = object: RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    binding.sliderImage.setImageResource(R.drawable.image_placeholder)
                    return true
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    return false
                }
            }
            binding.root.setOnClickListener {
                itemCallback?.invoke(item)
            }
            binding.root.setOnTouchListener { _, _ ->
                itemTouchCallback.invoke()
                true
            }
            binding.executePendingBindings()
        }
    }
}
