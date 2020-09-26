@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.widget

import android.graphics.drawable.Drawable
import android.widget.TabWidget
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.CustomSdkSetter
import dev.inkremental.dsl.android.SdkSetter
import dev.inkremental.dsl.android.initWith
import dev.inkremental.v
import kotlin.Boolean
import kotlin.Int
import kotlin.Suppress
import kotlin.Unit

fun tabWidget(configure: TabWidgetScope.() -> Unit = {}) =
    v<TabWidget>(configure.bind(TabWidgetScope))
abstract class TabWidgetScope : LinearLayoutScope() {
  override fun init(arg: (TabWidget) -> Unit): Unit = initWith<TabWidget>(arg)
  fun currentTab(arg: Int): Unit = attr("currentTab", arg)
  fun dividerDrawable(arg: Int): Unit = attr("dividerDrawable", arg)
  fun leftStripDrawable(arg: Drawable?): Unit = attr("leftStripDrawable", arg)
  fun leftStripDrawable(arg: Int): Unit = attr("leftStripDrawable", arg)
  fun rightStripDrawable(arg: Drawable?): Unit = attr("rightStripDrawable", arg)
  fun rightStripDrawable(arg: Int): Unit = attr("rightStripDrawable", arg)
  fun stripEnabled(arg: Boolean): Unit = attr("stripEnabled", arg)
  companion object : TabWidgetScope() {
    init {
      Inkremental.registerAttributeSetter(SdkSetter)
      Inkremental.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
