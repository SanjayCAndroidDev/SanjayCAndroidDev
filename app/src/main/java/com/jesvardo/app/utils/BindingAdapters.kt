package com.jesvardo.app.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.jesvardo.app.R

@BindingAdapter("loadImageUsingGlide")
fun loadImage(view: ImageView, imageUrl: String?) {

    Glide.with(view.context)
        .load(imageUrl)
        .centerCrop()
        .error(R.mipmap.ic_launcher_round)
        .placeholder(R.mipmap.ic_launcher_round)
        .into(view)

}