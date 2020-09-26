@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.androidx.appcompat.widget

import androidx.appcompat.widget.ScrollingTabContainerView
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.android.widget.HorizontalScrollViewScope
import dev.inkremental.dsl.androidx.appcompat.AppcompatV7Setter
import dev.inkremental.dsl.androidx.appcompat.CustomAppCompatv7Setter
import dev.inkremental.v
import kotlin.Boolean
import kotlin.Int
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

fun scrollingTabContainerView(configure: ScrollingTabContainerViewScope.() -> Unit = {}) =
    v<ScrollingTabContainerView>(configure.bind(ScrollingTabContainerViewScope))
abstract class ScrollingTabContainerViewScope : HorizontalScrollViewScope() {
  @JvmName("initScrollingTabContainerView")
  fun init(arg: (ScrollingTabContainerView) -> Unit): Unit =
      initWith<ScrollingTabContainerView>(arg)
  fun allowCollapse(arg: Boolean): Unit = attr("allowCollapse", arg)
  fun contentHeight(arg: Int): Unit = attr("contentHeight", arg)
  fun tabSelected(arg: Int): Unit = attr("tabSelected", arg)
  companion object : ScrollingTabContainerViewScope() {
    init {
      Inkremental.registerAttributeSetter(AppcompatV7Setter)
      Inkremental.registerAttributeSetter(CustomAppCompatv7Setter)
    }
  }
}
