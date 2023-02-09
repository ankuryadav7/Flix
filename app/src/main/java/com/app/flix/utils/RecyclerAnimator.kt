package com.app.flix.utils

import android.app.Activity
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.app.flix.R

/**
 * Used for animate entry items of recyclerview
 * */
class RecyclerAnimator(val activity: Activity?) {
    fun animate(myRecyclerView: RecyclerView) {
        val resId: Int = R.anim.layout_animation_fall_down
        if (activity == null) {
            return
        }
        val animation = AnimationUtils.loadLayoutAnimation(activity, resId)
        myRecyclerView.layoutAnimation = animation
    }
}