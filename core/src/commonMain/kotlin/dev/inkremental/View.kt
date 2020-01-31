package dev.inkremental

expect open class View
expect abstract class ViewGroup: View

expect val View.context: Context

expect val ViewGroup.children: Sequence<View>
expect val ViewGroup.childrenCount: Int
expect fun ViewGroup.getChildAt(position: Int): View
expect fun ViewGroup.addView(view: View?, position: Int)
expect fun ViewGroup.removeView(view: View?)
expect fun ViewGroup.removeAllChildren()

expect abstract class Context

expect fun initPlatform()
