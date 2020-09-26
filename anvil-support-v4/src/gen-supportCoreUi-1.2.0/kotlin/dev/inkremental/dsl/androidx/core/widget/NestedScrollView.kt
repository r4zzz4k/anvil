@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.androidx.core.widget

import androidx.core.widget.NestedScrollView
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.android.widget.FrameLayoutScope
import dev.inkremental.dsl.androidx.core.CustomSupportV4Setter
import dev.inkremental.dsl.androidx.core.SupportCoreUiSetter
import dev.inkremental.v
import kotlin.Boolean
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

fun nestedScrollView(configure: NestedScrollViewScope.() -> Unit = {}) =
    v<NestedScrollView>(configure.bind(NestedScrollViewScope))
abstract class NestedScrollViewScope : FrameLayoutScope() {
  @JvmName("initNestedScrollView")
  fun init(arg: (NestedScrollView) -> Unit): Unit = initWith<NestedScrollView>(arg)
  fun fillViewport(arg: Boolean): Unit = attr("fillViewport", arg)
  fun smoothScrollingEnabled(arg: Boolean): Unit = attr("smoothScrollingEnabled", arg)
  companion object : NestedScrollViewScope() {
    init {
      Inkremental.registerAttributeSetter(SupportCoreUiSetter)
      Inkremental.registerAttributeSetter(CustomSupportV4Setter)
    }
  }
}
