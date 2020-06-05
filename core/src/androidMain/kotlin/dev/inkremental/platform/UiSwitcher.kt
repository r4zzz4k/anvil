package dev.inkremental.platform

import android.os.Handler
import android.os.Looper
import dev.inkremental.Inkremental

actual class UiSwitcher actual constructor(action: () -> Unit) {
    private val uiHandler: Handler by lazy { Handler(Looper.getMainLooper()) }
    private val runnable = Runnable { action() }

    init {
        Inkremental.registerViewFactory(DefaultViewFactory())
        Inkremental.registerAttributeSetter(ReflectivePropertySetter())
    }

    actual fun scheduleOnUi(): Boolean {
        if (Looper.myLooper() == Looper.getMainLooper()) return false
        uiHandler.removeCallbacksAndMessages(null)
        uiHandler.post(runnable)
        return true
    }
}
