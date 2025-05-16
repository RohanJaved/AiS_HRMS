package com.appinsnap.aishrm.util

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

class TouchInterceptor(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // Intercept all touch events and return true to prevent them from being passed to child views
        return true
    }
}