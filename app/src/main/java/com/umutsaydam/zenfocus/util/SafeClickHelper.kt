package com.umutsaydam.zenfocus.util

/**
 * It is okay, If ClickExtensions will use only one specific click process
 * but need multi click processes in one screen, then you should switch to SafeClick with compose method
 */
object SafeClickHelper {
    private const val DEFAULT_DELAY = 800L
    private var lastClickTime = 0L

    fun canClick(delay: Long = DEFAULT_DELAY): Boolean {
        val currentTime = System.currentTimeMillis()
        val diff = currentTime - lastClickTime

        return if (diff > delay) {
            lastClickTime = diff
            true
        } else {
            false
        }
    }
}