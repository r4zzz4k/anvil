package dev.inkremental

import android.content.Context as AndroidContext
import android.view.View as AndroidView
import android.view.ViewGroup as AndroidViewGroup

actual typealias View = AndroidView
actual typealias ViewGroup = AndroidViewGroup

actual val View.context: Context
    get() = context

actual val ViewGroup.children: Sequence<View>
    get() = sequence {
        for(i in 0 until childCount) {
            yield(getChildAt(i))
        }
    }

actual val ViewGroup.childrenCount: Int
    get() = this.childCount

actual fun ViewGroup.getChildAt(position: Int): View = getChildAt(position)!!
actual fun ViewGroup.addView(view: View?, position: Int) = addView(view, position)
actual fun ViewGroup.removeView(view: View?) = removeView(view)

actual fun ViewGroup.removeAllChildren() {
    // TODO why not removeAllViews()?
    removeViews(0, childCount)
}

actual typealias Context = AndroidContext

actual fun initPlatform() {
    Inkremental.registerViewFactory(DefaultViewFactory())
    Inkremental.registerAttributeSetter(ReflectiveSetter())
}
