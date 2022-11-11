package com.example.template.data.binding
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

//import com.bumptech.glide.Glide
import com.example.template.R

/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    if(imgUrl is String){
        Glide.with(imgView.context)
                .load(imgUrl)
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
                .into(imgView)
    }
}