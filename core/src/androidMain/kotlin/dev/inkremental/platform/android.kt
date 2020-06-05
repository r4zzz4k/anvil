package dev.inkremental.platform

import dev.inkremental.*
import android.view.View as AndroidView
import android.view.ViewGroup as AndroidViewGroup

internal actual fun platformInit() {
    Inkremental.registerViewFactory(DefaultViewFactory())
    Inkremental.registerAttributeSetter(ReflectivePropertySetter())
}

actual typealias View = AndroidView
actual typealias ViewGroup = AndroidViewGroup

actual val ViewGroup.childrenCount: Int
    get() = this.childCount

actual fun ViewGroup.childAt(position: Int): View = getChildAt(position)

actual fun ViewGroup.addChild(child: View, position: Int) {
    addView(child, position)
}

actual fun ViewGroup.removeChild(child: View?) {
    removeView(child)
}

actual fun ViewGroup.clearChildren() {
    removeAllViews()
    //removeViews(0, childCount)
}
