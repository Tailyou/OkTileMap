package com.hengda.zwf.oktilemap.util

import android.content.Context
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget

class GlideImageLoader {

    fun displayImage(context: Context, path: String, imageView: ImageView) {
        Glide.with(context)
                .load(path)
                .into<SimpleTarget<GlideDrawable>>(object : SimpleTarget<GlideDrawable>() {
                    override fun onResourceReady(resource: GlideDrawable, glideAnimation: GlideAnimation<in GlideDrawable>) {
                        imageView.setImageDrawable(resource)
                    }
                })
    }

    fun displayImage(context: Context, path: String, imageView: ImageView, width: Int, height: Int) {
        Glide.with(context)
                .load(path)
                .override(width, height)
                .into<SimpleTarget<GlideDrawable>>(object : SimpleTarget<GlideDrawable>() {
                    override fun onResourceReady(resource: GlideDrawable, glideAnimation: GlideAnimation<in GlideDrawable>) {
                        imageView.setImageDrawable(resource)
                    }
                })
    }

}
