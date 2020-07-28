package com.huawei.references.hinotes.ui.itemdetail.reminder

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior


class LockableBottomSheetBehavior<V : View?> : BottomSheetBehavior<V> {
    private var mLocked = false

    constructor() {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}

    fun setLocked(locked: Boolean) {
        mLocked = locked
    }

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: V,
        event: MotionEvent
    ): Boolean {
        var handled = false
        if (!mLocked) {
            handled = super.onInterceptTouchEvent(parent, child, event)
        }
        return handled
    }

    override fun onTouchEvent(
        parent: CoordinatorLayout,
        child: V,
        event: MotionEvent
    ): Boolean {
        var handled = false
        if (!mLocked) {
            handled = super.onTouchEvent(parent, child, event)
        }
        return handled
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        if(!mLocked)
        return super.onStartNestedScroll(
            coordinatorLayout,
            child,
            directTargetChild,
            target,
            axes,
            type
        )
        return false
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        type: Int
    ) {
        if (!mLocked)
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
    }

    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        var handled = false
        if(!mLocked)
        handled = super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
        return handled
    }
}