package dev.inkremental

expect class UiHandler constructor() {
    fun runOnUiThread(action: () -> Unit)
}

