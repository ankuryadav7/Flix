package com.app.flix.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.app.flix.R

/**
 * Used for handling images that comes from local or server
 * */
object ImageHelper {
    private var mGlideObj: Glide? = null
    private var requestOptions: RequestOptions? = null
    var resource: Int = 0

    fun init(context: Context?) : ImageHelper{
        mGlideObj = Glide.get(context!!)
        requestOptions = RequestOptions()
        return this
    }

    fun loadImageTo(imageView: ImageView?, poster: String) {
        /**
        we are getting poster name from model
        on the basis of name we are fetching the image drawable and display on imageview
        */
        resource=when(poster) {
            "poster1.jpg"->R.drawable.poster1
            "poster2.jpg"->R.drawable.poster2
            "poster3.jpg"->R.drawable.poster3
            "poster4.jpg"->R.drawable.poster4
            "poster5.jpg"->R.drawable.poster5
            "poster6.jpg"->R.drawable.poster6
            "poster7.jpg"->R.drawable.poster7
            "poster8.jpg"->R.drawable.poster8
            "poster9.jpg"->R.drawable.poster9
             else -> R.drawable.place_holder
        }

        /** glide for displaying image */
        Glide.with(mGlideObj!!.context).setDefaultRequestOptions(requestOptions!!).load(resource)
            .transition(DrawableTransitionOptions.withCrossFade(250)).into(
                imageView!!
            )
    }
}