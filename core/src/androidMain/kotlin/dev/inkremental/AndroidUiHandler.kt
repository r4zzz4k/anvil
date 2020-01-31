package dev.inkremental

import android.os.Handler
import android.os.Looper

actual class UiHandler actual constructor() {
    private val handler: Handler by lazy { Handler(Looper.getMainLooper()) }

    actual fun runOnUiThread(action: () -> Unit) {
        if(Looper.myLooper() != Looper.getMainLooper()) {
            handler.removeCallbacksAndMessages(null)
            handler.post(action)
        } else {
            action()
        }
    }
}
