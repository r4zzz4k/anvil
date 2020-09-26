@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.widget

import android.widget.TabHost
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.CustomSdkSetter
import dev.inkremental.dsl.android.SdkSetter
import dev.inkremental.dsl.android.initWith
import dev.inkremental.v
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit

fun tabHost(configure: TabHostScope.() -> Unit = {}) = v<TabHost>(configure.bind(TabHostScope))
abstract class TabHostScope : FrameLayoutScope() {
  override fun init(arg: (TabHost) -> Unit): Unit = initWith<TabHost>(arg)
  fun currentTab(arg: Int): Unit = attr("currentTab", arg)
  fun currentTabByTag(arg: String): Unit = attr("currentTabByTag", arg)
  fun onTabChanged(arg: ((arg0: String) -> Unit)?): Unit = attr("onTabChanged", arg)
  companion object : TabHostScope() {
    init {
      Inkremental.registerAttributeSetter(SdkSetter)
      Inkremental.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
