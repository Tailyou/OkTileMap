package com.hengda.hdtilemap.anim

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import com.pawegio.kandroid.animListener

/**
 * 跳动动效-参考自 HDTileView（by 白世伟）
 * @author 祝文飞（Tailyou）
 * @time 2017/4/28 15:58
 */
class Jumper(duration: Long, offset: Float) {

    private val mAniDown: Animation
    private val mAniUp: Animation
    private var mView: View? = null

    init {
        mAniUp = TranslateAnimation(0f, 0f, 0f, -offset)
        mAniUp.interpolator = DecelerateInterpolator()
        mAniUp.duration = duration
        mAniDown = TranslateAnimation(0f, 0f, -offset, 0f)
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