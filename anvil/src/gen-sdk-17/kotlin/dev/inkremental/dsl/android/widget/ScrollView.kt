@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.widget

import android.widget.ScrollView
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.CustomSdkSetter
import dev.inkremental.dsl.android.SdkSetter
import dev.inkremental.dsl.android.initWith
import dev.inkremental.v
import kotlin.Boolean
import kotlin.Suppress
import kotlin.Unit

fun scrollView(configure: ScrollViewScope.() -> Unit = {}) =
    v<ScrollView>(configure.bind(ScrollViewScope))
abstract class ScrollViewScope : FrameLayoutScope() {
  override fun init(arg: (ScrollView) -> Unit): Unit = initWith<ScrollView>(arg)
  fun fillViewport(arg: Boolean): Unit = attr("fillViewport", arg)
  fun smoothScrollingEnabled(arg: Boolean): Unit = attr("smoothScrollingEnabled", arg)
  companion object : ScrollViewScope() {
    init {
      Inkremental.registerAttributeSetter(SdkSetter)
      Inkremental.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
