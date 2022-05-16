package com.example.flipanimationtest

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val container = findViewById<ConstraintLayout>(R.id.container)
        val cardView = findViewById<CardView>(R.id.card)
        val view1 = findViewById<ConstraintLayout>(R.id.view1)
        val view2 = findViewById<ConstraintLayout>(R.id.view2)

        var isFront = true
        cardView.setOnClickListener {
            doFlipAnimation(
                isFrontToBack = isFront,
                targetView = container,
                onAngle90 = {
                    view1.visibility = if (isFront) View.GONE else View.VISIBLE
                    view2.visibility = if (isFront) View.VISIBLE else View.GONE
                },
                onAnimationEnd = {
                    isFront = !isFront
                    Log.i("[MAIN]", "do Action")
                }
            )
        }
    }

    /**
     * Flip 애니메이션 수행
     * @param isFrontToBack 애니메이션 방향 결정 true: 시계 방향 또는 false: 반시계 방향
     * @param targetView flip 에니매이션을 적용할 View
     * @param onAngle90 90 도(애니메이션 중간 지점) 에 도달했을 시 콜백
     * @param onAnimationEnd 애니메이션 완료 콜백
     */
    private fun doFlipAnimation(
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
            addEndListener { onAnimationEnd?.invoke() }
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
}