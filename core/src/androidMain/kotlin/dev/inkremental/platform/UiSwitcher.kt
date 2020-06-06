package dev.inkremental.platform

import android.os.Handler
import android.os.Looper
import dev.inkremental.Inkremental
import dev.inkremental.android.AndroidViewFactory
import dev.inkremental.android.ReflectivePropertySetter

actual class UiSwitcher actual constructor(action: () -> Unit) {
    private val uiHandler: Handler by lazy { Handler(Looper.getMainLooper()) }
    private val runnable = Runnable { action() }

    init {
        Inkremental.registerViewFactory(AndroidViewFactory())
        Inkremental.registerAttributeSetter(ReflectivePropertySetter())
    }

    actual fun scheduleOnUi(): Boolean {
        if (Looper.myLooper() == Looper.getMainLooper()) return false
        uiHandler.removeCallbacksAndMessages(null)
        uiHandler.post(runnable)
        return true
    }
}
