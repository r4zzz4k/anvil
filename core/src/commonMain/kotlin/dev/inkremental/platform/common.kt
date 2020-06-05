package dev.inkremental.platform

internal expect fun platformInit()

expect class UiSwitcher(action: () -> Unit) {
    actual fun scheduleOnUi(): Boolean
}

expect fun <T, U> WeakHashMap(): MutableMap<T, U>

expect class WeakReference<T>(referent: T) {
    fun get(): T?
}

expect open class View

expect abstract class ViewGroup : View
expect val ViewGroup.childrenCount: Int
expect fun ViewGroup.childAt(position: Int): View
expect fun ViewGroup.addChild(child: View, position: Int)
expect fun ViewGroup.removeChild(child: View?)
expect fun ViewGroup.clearChildren()
