package com.example.flipanimationtest

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.flipanimationtest.databinding.ActivityMainBinding
import kotlin.math.abs


class MainActivity : AppCompatActivity() {
    private lateinit var dataBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initializeViewPager()

        var isFront = true
        dataBinding.flipButton.setOnClickListener {
            doFlipAnimation1(
                isFrontToBack = isFront,
                targetView = dataBinding.container,
                onAngle90 = {
                    dataBinding.view1.visibility = if (isFront) View.INVISIBLE else View.VISIBLE
                    dataBinding.viewPager.visibility = if (isFront) View.VISIBLE else View.INVISIBLE
                },
                onAnimationEnd = {
                    isFront = !isFront
                    Log.i("[MAIN]", "do Action")
                }
            )

            doFlipAnimation2(
                targetView = dataBinding.container2,
                onAngle90 = {
                    dataBinding.view12.visibility = if (isFront) View.INVISIBLE else View.VISIBLE
                    dataBinding.view22.visibility = if (isFront) View.VISIBLE else View.INVISIBLE
                },
                onAnimationEnd = {}
            )
        }
    }

    private fun initializeViewPager() {
        dataBinding.viewPager.apply {
            adapter = CardPagerAdapter(listOf("card1", "card2", "card3", "card4", "card5"))
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            offscreenPageLimit = 3
            (getChildAt(0) as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
            CompositePageTransformer().apply {
                addTransformer { view: View, fl: Float ->
                    val v = 1 - abs(fl)
                    view.scaleY = 0.7f + v * 0.3f
                    view.translationY = abs(fl) * 20
                }
            }.also {
                setPageTransformer(it)
            }
        }
    }

}