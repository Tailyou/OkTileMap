package com.hengda.hdtilemap.anim

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import com.pawegio.kandroid.animListener

class Jumper(duration: Long, offset: Int) {

    private val mAniDown: Animation
    private val mAniUp: Animation
    private var mView: View? = null

    init {
        mAniUp = TranslateAnimation(0f, 0f, 0f, (-offset).toFloat())
        mAniUp.interpolator = DecelerateInterpolator()
        mAniUp.duration = duration
        mAniDown = TranslateAnimation(0f, 0f, (-offset).toFloat(), 0f)
        mAniDown.interpolator = AccelerateInterpolator()
        mAniDown.duration = duration
        mAniUp.animListener {
            onAnimationEnd {
                mView?.startAnimation(mAniDown)
            }
        }
        mAniDown.animListener {
            onAnimationEnd {
                mView?.startAnimation(mAniUp)
            }
        }
    }

    fun attachToView(view: View) {
        mView = view
        mView?.startAnimation(mAniUp)
    }

}