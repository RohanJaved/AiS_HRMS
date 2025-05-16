package com.appinsnap.aishrm.base.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.appinsnap.aishrm.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

object DataBindingAdapters {
    @BindingAdapter("imageResource")
    fun setImageResource(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)
    }

}