package com.example.flipanimationtest

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator

/**
 * Flip 애니메이션 수행
 * @param isFrontToBack 애니메이션 방향 결정 true: 시계 방향 또는 false: 반시계 방향
 * @param targetView flip 에니매이션을 적용할 View
 * @param onAngle90 90 도(애니메이션 중간 지점) 에 도달했을 시 콜백
 * @param onAnimationEnd 애니메이션 완료 콜백
 */
fun doFlipAnimation1(
    isFrontToBack: Boolean,
    targetView: View,
    onAngle90: (() -> Unit)? = null,
    onAnimationEnd: (() -> Unit)? = null
) {
    val list = if (isFrontToBack) floatArrayOf(0f, 90f, 270f, 360f) else floatArrayOf(360f, 270f, 90f, 0f)
    val anim1 = ObjectAnimator.ofFloat(targetView, "rotationY", list[0], list[1]).apply {
        interpolator = AccelerateInterpolator()
        addEndListener { onAngle90?.invoke() }
    }
    val anim2 = ObjectAnimator.ofFloat(targetView, "rotationY", list[2], list[3]).apply {
        interpolator = AccelerateDecelerateInterpolator()
    }
    AnimatorSet().apply {
        play(anim1).before(anim2)
        play(anim1).with(ObjectAnimator.ofFloat(targetView, "scaleX", 1f, 0.5f))
        play(anim2).with(ObjectAnimator.ofFloat(targetView, "scaleX", 0.5f, 1f))
        addEndListener {
            targetView.translationY = 0f
            targetView.scaleX = 1f
            onAnimationEnd?.invoke()
        }
    }.start()
}

/**
 * Flip 애니메이션 수행
 * @param targetView flip 에니매이션을 적용할 View
 * @param onAngle90 90 도(애니메이션 중간 지점) 에 도달했을 시 콜백
 * @param onAnimationEnd 애니메이션 완료 콜백
 */
fun doFlipAnimation2(
    targetView: View,
    onAngle90: (() -> Unit)? = null,
    onAnimationEnd: (() -> Unit)? = null
) {
    val anim1 = ObjectAnimator.ofFloat(targetView, "scaleX", 1f, 0f).apply {
        interpolator = AccelerateInterpolator()
        duration = 300
        addEndListener { onAngle90?.invoke() }
    }
    val anim2 = ObjectAnimator.ofFloat(targetView, "scaleX", 0f, 1f).apply {
        duration = 300
        interpolator = AccelerateDecelerateInterpolator()
    }
    AnimatorSet().apply {
        play(anim1).before(anim2)
        addEndListener {
            targetView.scaleX = 1f
            onAnimationEnd?.invoke()
        }
    }.start()
}

/**
 * 애니메이션 끝날을 시 리스너 설정
 */
private fun Animator.addEndListener(callback: (Animator)->Unit) {
    this.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
            callback(animation)
        }
    })
}